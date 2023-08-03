
package org.jooby.handlers;
import com.google.common.base.Strings;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;
import static java.util.Objects.requireNonNull;
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
  public AssetHandler(final String pattern, final ClassLoader loader) {
    init(Route.normalize(pattern), Paths.get("public"), loader);
  }
  public AssetHandler(final Path basedir) {
    init("/{0}", basedir, getClass().getClassLoader());
  }
  public AssetHandler(final String pattern) {
    init(Route.normalize(pattern), Paths.get("public"), getClass().getClassLoader());
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
  private void init(final String pattern, final Path basedir, final ClassLoader loader) {
    requireNonNull(loader, "Resource loader is required.");
    this.fn = pattern.equals("/")
        ? (req, p) -> prefix.apply(p)
        : (req, p) -> MessageFormat.format(prefix.apply(pattern), vars(req));
    this.loader = loader(basedir, loader);
  }
  private static Object[] vars(final Request req) {
    Map<Object, String> vars = req.route().vars();
    return vars.values().toArray(new Object[vars.size()]);
  }
  private static Loader loader(final Path basedir, final ClassLoader classloader) {
    if (Files.exists(basedir)) {
      return name -> {
        Path path = basedir.resolve(name).normalize();
        if (Files.exists(path) && path.startsWith(basedir)) {
          try {
            return path.toUri().toURL();
          } catch (MalformedURLException x) {
          }
        }
        return classloader.getResource(name);
      };
    }
    return classloader::getResource;
  }
  private static Throwing.Function<String, String> prefix() {
    return p -> p.substring(1);
  }
}
