
package org.jooby.handlers;
import com.google.common.base.Strings;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;
import org.jooby.Asset;
import org.jooby.Err;
import org.jooby.Jooby;
import org.jooby.MediaType;
import org.jooby.Request;
import org.jooby.Response;
import org.jooby.Route;
import org.jooby.Status;
import org.jooby.funzy.Throwing;
import org.jooby.funzy.Try;
import org.jooby.internal.URLAsset;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import static java.util.Objects.requireNonNull;
public class AssetHandler implements Route.Handler {
  private interface Loader {
    URL getResource(String name);
  }
  private static final Throwing.Function<String, String> prefix = prefix().memoized();
  private Throwing.Function2<Request, String, String> fn;
  private Loader loader;
  private String cdn;
  private boolean etag = true;
  private long maxAge = -1;
  private boolean lastModified = true;
  private int statusCode = 404;
  private String location;
  private Path basedir;
  private ClassLoader classLoader;
  public AssetHandler(final String pattern, final ClassLoader loader) {
    this.location = Route.normalize(pattern);
    this.basedir = Paths.get("public");
    this.classLoader = loader;
  }
  public AssetHandler(final Path basedir) {
    this.location = "/{0}";
    this.basedir = basedir;
    this.classLoader = getClass().getClassLoader();
  }
  public AssetHandler(final String pattern) {
    this.location = Route.normalize(pattern);
    this.basedir = Paths.get("public");
    this.classLoader = getClass().getClassLoader();
  }
  public AssetHandler etag(final boolean etag) {
    this.etag = etag;
    return this;
  }
  public AssetHandler lastModified(final boolean enabled) {
    this.lastModified = enabled;
    return this;
  }
  public AssetHandler cdn(final String cdn) {
    this.cdn = Strings.emptyToNull(cdn);
    return this;
  }
  public AssetHandler maxAge(final Duration maxAge) {
    return maxAge(maxAge.getSeconds());
  }
  public AssetHandler maxAge(final long maxAge) {
    this.maxAge = maxAge;
    return this;
  }
  public AssetHandler setRoute(final Route.AssetDefinition route) {
    String prefix;
    boolean rootLocation = location.equals("/") || location.equals("/{0}");
    if (rootLocation) {
      String pattern = route.pattern();
      int i = pattern.indexOf("
  public AssetHandler maxAge(final String maxAge) {
    Try.apply(() -> Long.parseLong(maxAge))
        .recover(x -> ConfigFactory.empty()
            .withValue("v", ConfigValueFactory.fromAnyRef(maxAge))
            .getDuration("v")
            .getSeconds())
        .onSuccess(this::maxAge);
    return this;
  }
  public AssetHandler onMissing(final int statusCode) {
    this.statusCode = statusCode;
    return this;
  }
  @Override
  public void handle(final Request req, final Response rsp) throws Throwable {
    String path = req.path();
    URL resource = resolve(req, path);
    if (resource != null) {
      String localpath = resource.getPath();
      int jarEntry = localpath.indexOf("!/");
      if (jarEntry > 0) {
        localpath = localpath.substring(jarEntry + 2);
      }
      URLAsset asset = new URLAsset(resource, path,
          MediaType.byPath(localpath).orElse(MediaType.octetstream));
      if (asset.exists()) {
        if (cdn != null) {
          String absUrl = cdn + path;
          rsp.redirect(absUrl);
          rsp.end();
        } else {
          doHandle(req, rsp, asset);
        }
      }
    } else if (statusCode > 0) {
      throw new Err(statusCode);
    }
  }
  private void doHandle(final Request req, final Response rsp, final Asset asset) throws Throwable {
    if (this.etag) {
      String etag = asset.etag();
      boolean ifnm = req.header("If-None-Match").toOptional()
          .map(etag::equals)
          .orElse(false);
      if (ifnm) {
        rsp.header("ETag", etag).status(Status.NOT_MODIFIED).end();
        return;
      }
      rsp.header("ETag", etag);
    }
    if (this.lastModified) {
      long lastModified = asset.lastModified();
      if (lastModified > 0) {
        boolean ifm = req.header("If-Modified-Since").toOptional(Long.class)
            .map(ifModified -> lastModified / 1000 <= ifModified / 1000)
            .orElse(false);
        if (ifm) {
          rsp.status(Status.NOT_MODIFIED).end();
          return;
        }
        rsp.header("Last-Modified", new Date(lastModified));
      }
    }
    if (maxAge > 0) {
      rsp.header("Cache-Control", "max-age=" + maxAge);
    }
    send(req, rsp, asset);
  }
  protected void send(final Request req, final Response rsp, final Asset asset) throws Throwable {
    rsp.send(asset);
  }
  private URL resolve(final Request req, final String path) throws Throwable {
    String target = fn.apply(req, path);
    return resolve(target);
  }
  protected URL resolve(final String path) throws Exception {
    return loader.getResource(path);
  }
  private void init(final String classPathPrefix, final String location, final Path basedir,
      final ClassLoader loader) {
    requireNonNull(loader, "Resource loader is required.");
    this.fn = location.equals("/")
        ? (req, p) -> prefix.apply(p)
        : (req, p) -> MessageFormat.format(prefix.apply(location), vars(req));
    this.loader = loader(basedir, classpathLoader(classPathPrefix, classLoader));
  }
  private static Object[] vars(final Request req) {
    Map<Object, String> vars = req.route().vars();
    return vars.values().toArray(new Object[vars.size()]);
  }
  private static Loader loader(final Path basedir, Loader classpath) {
    if (basedir != null && Files.exists(basedir)) {
      return name -> {
        Path path = basedir.resolve(name).normalize();
        if (Files.exists(path) && path.startsWith(basedir)) {
          try {
            return path.toUri().toURL();
          } catch (MalformedURLException x) {
          }
        }
        return classpath.getResource(name);
      };
    }
    return classpath;
  }
  private static Loader classpathLoader(String prefix, ClassLoader classloader) {
    return name -> {
      String safePath = safePath(name);
      if (safePath.startsWith(prefix)) {
        URL resource = classloader.getResource(safePath);
        return resource;
      }
      return null;
    };
  }
  private static String safePath(String name) {
    if (name.indexOf("./") > 0) {
      Path path = toPath(name.split("/")).normalize();
      return toStringPath(path);
    }
    return name;
  }
  private static String toStringPath(Path path) {
    StringBuilder buffer = new StringBuilder();
    for (Path segment : path) {
      buffer.append("/").append(segment);
    }
    return buffer.substring(1);
  }
  private static Path toPath(String[] segments) {
    Path path = Paths.get(segments[0]);
    for (int i = 1; i < segments.length; i++) {
      path = path.resolve(segments[i]);
    }
    return path;
  }
  private static Throwing.Function<String, String> prefix() {
    return p -> p.substring(1);
  }
}
