
package org.jooby;
import com.google.common.base.Joiner;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.escape.Escaper;
import com.google.common.html.HtmlEscapers;
import com.google.common.net.UrlEscapers;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.ProviderMethodsModule;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.google.inject.util.Types;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;
import static com.typesafe.config.ConfigValueFactory.fromAnyRef;
import static java.util.Objects.requireNonNull;
import static org.jooby.Route.CONNECT;
import static org.jooby.Route.DELETE;
import org.jooby.Route.Definition;
import static org.jooby.Route.GET;
import static org.jooby.Route.HEAD;
import org.jooby.Route.Mapper;
import static org.jooby.Route.OPTIONS;
import static org.jooby.Route.PATCH;
import static org.jooby.Route.POST;
import static org.jooby.Route.PUT;
import static org.jooby.Route.TRACE;
import org.jooby.Session.Store;
import org.jooby.funzy.Throwing;
import org.jooby.funzy.Try;
import org.jooby.handlers.AssetHandler;
import org.jooby.internal.AppPrinter;
import org.jooby.internal.BuiltinParser;
import org.jooby.internal.BuiltinRenderer;
import org.jooby.internal.CookieSessionManager;
import org.jooby.internal.DefaulErrRenderer;
import org.jooby.internal.HttpHandlerImpl;
import org.jooby.internal.JvmInfo;
import org.jooby.internal.LocaleUtils;
import org.jooby.internal.ParameterNameProvider;
import org.jooby.internal.RequestScope;
import org.jooby.internal.RouteMetadata;
import org.jooby.internal.ServerExecutorProvider;
import org.jooby.internal.ServerLookup;
import org.jooby.internal.ServerSessionManager;
import org.jooby.internal.SessionManager;
import org.jooby.internal.SourceProvider;
import org.jooby.internal.TypeConverters;
import org.jooby.internal.handlers.HeadHandler;
import org.jooby.internal.handlers.OptionsHandler;
import org.jooby.internal.handlers.TraceHandler;
import org.jooby.internal.mvc.MvcRoutes;
import org.jooby.internal.mvc.MvcWebSocket;
import org.jooby.internal.parser.BeanParser;
import org.jooby.internal.parser.DateParser;
import org.jooby.internal.parser.LocalDateParser;
import org.jooby.internal.parser.LocaleParser;
import org.jooby.internal.parser.ParserExecutor;
import org.jooby.internal.parser.StaticMethodParser;
import org.jooby.internal.parser.StringConstructorParser;
import org.jooby.internal.parser.ZonedDateTimeParser;
import org.jooby.internal.ssl.SslContextProvider;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.Produces;
import org.jooby.scope.Providers;
import org.jooby.scope.RequestScoped;
import org.jooby.spi.HttpHandler;
import org.jooby.spi.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
public class Jooby implements Router, LifeCycle, Registry {
  public interface EnvPredicate {
    default void orElse(final Runnable callback) {
      orElse(conf -> callback.run());
    }
    void orElse(Consumer<Config> callback);
  }
  public interface Module {
    @Nonnull
    default Config config() {
      return ConfigFactory.empty();
    }
    void configure(Env env, Config conf, Binder binder) throws Throwable;
  }
  static class MvcClass implements Route.Props<MvcClass> {
    Class<?> routeClass;
    String path;
    ImmutableMap.Builder<String, Object> attrs = ImmutableMap.builder();
    private List<MediaType> consumes;
    private String name;
    private List<MediaType> produces;
    private List<String> excludes;
    private Mapper<?> mapper;
    private String prefix;
    private String renderer;
    public MvcClass(final Class<?> routeClass, final String path, final String prefix) {
      this.routeClass = routeClass;
      this.path = path;
      this.prefix = prefix;
    }
    @Override
    public MvcClass attr(final String name, final Object value) {
      attrs.put(name, value);
      return this;
    }
    @Override
    public MvcClass name(final String name) {
      this.name = name;
      return this;
    }
    @Override
    public MvcClass consumes(final List<MediaType> consumes) {
      this.consumes = consumes;
      return this;
    }
    @Override
    public MvcClass produces(final List<MediaType> produces) {
      this.produces = produces;
      return this;
    }
    @Override
    public MvcClass excludes(final List<String> excludes) {
      this.excludes = excludes;
      return this;
    }
    @Override
    public MvcClass map(final Mapper<?> mapper) {
      this.mapper = mapper;
      return this;
    }
    @Override
    public String renderer() {
      return renderer;
    }
    @Override
    public MvcClass renderer(final String name) {
      this.renderer = name;
      return this;
    }
    public Route.Definition apply(final Route.Definition route) {
      attrs.build().forEach(route::attr);
      if (name != null) {
        route.name(name);
      }
      if (prefix != null) {
        route.name(prefix + "/" + route.name());
      }
      if (consumes != null) {
        route.consumes(consumes);
      }
      if (produces != null) {
        route.produces(produces);
      }
      if (excludes != null) {
        route.excludes(excludes);
      }
      if (mapper != null) {
        route.map(mapper);
      }
      if (renderer != null) {
        route.renderer(renderer);
      }
      return route;
    }
  }
  private static class EnvDep {
    Predicate<String> predicate;
    Consumer<Config> callback;
    public EnvDep(final Predicate<String> predicate, final Consumer<Config> callback) {
      this.predicate = predicate;
      this.callback = callback;
    }
  }
  static {
    String pid = System.getProperty("pid", JvmInfo.pid() + "");
    System.setProperty("pid", pid);
  }
  private transient Set<Object> bag = new LinkedHashSet<>();
  private transient Config srcconf;
  private final transient AtomicBoolean started = new AtomicBoolean(false);
  private transient Injector injector;
  private transient Session.Definition session = new Session.Definition(Session.Mem.class);
  private transient Env.Builder env = Env.DEFAULT;
  private transient String prefix;
  private transient List<Throwing.Consumer<Registry>> onStart = new ArrayList<>();
  private transient List<Throwing.Consumer<Registry>> onStarted = new ArrayList<>();
  private transient List<Throwing.Consumer<Registry>> onStop = new ArrayList<>();
  @SuppressWarnings("rawtypes")
  private transient Mapper mapper;
  private transient Set<String> mappers = new HashSet<>();
  private transient Optional<Parser> beanParser = Optional.empty();
  private transient ServerLookup server = new ServerLookup();
  private transient String dateFormat;
  private transient Charset charset;
  private transient String[] languages;
  private transient ZoneId zoneId;
  private transient Integer port;
  private transient Integer securePort;
  private transient String numberFormat;
  private transient boolean http2;
  private transient List<Consumer<Binder>> executors = new ArrayList<>();
  private transient boolean defaultExecSet;
  private boolean throwBootstrapException;
  private transient BiFunction<Stage, com.google.inject.Module, Injector> injectorFactory = Guice::createInjector;
  private transient List<Jooby> apprefs;
  private transient LinkedList<String> path = new LinkedList<>();
  private transient String confname;
  private transient boolean caseSensitiveRouting = true;
  private transient String classname;
  public Jooby() {
    this(null);
  }
  public Jooby(final String prefix) {
    this.prefix = prefix;
    use(server);
    this.classname = classname(getClass().getName());
  }
  @Override
  public Route.Collection path(String path, Runnable action) {
    this.path.addLast(Route.normalize(path));
    Route.Collection collection = with(action);
    this.path.removeLast();
    return collection;
  }
  @Override
  public Jooby use(final Jooby app) {
    return use(prefixPath(null), app);
  }
  private Optional<String> prefixPath(@Nullable String tail) {
    return path.size() == 0
        ? tail == null ? Optional.empty() : Optional.of(Route.normalize(tail))
        : Optional.of(path.stream()
        .collect(Collectors.joining("", "", tail == null
            ? "" : Route.normalize(tail))));
  }
  @Override
  public Jooby use(final String path, final Jooby app) {
    return use(prefixPath(path), app);
  }
  public Jooby server(final Class<? extends Server> server) {
    requireNonNull(server, "Server required.");
    List<Object> tmp = bag.stream()
        .skip(1)
        .collect(Collectors.toList());
    tmp.add(0,
        (Module) (env, conf, binder) -> binder.bind(Server.class).to(server).asEagerSingleton());
    bag.clear();
    bag.addAll(tmp);
    return this;
  }
  private Jooby use(final Optional<String> path, final Jooby app) {
    requireNonNull(app, "App is required.");
    Function<Route.Definition, Route.Definition> rewrite = r -> {
      return path.map(p -> {
        Route.Definition result = new Route.Definition(r.method(), p + r.pattern(), r.filter());
        result.consumes(r.consumes());
        result.produces(r.produces());
        result.excludes(r.excludes());
        return result;
      }).orElse(r);
    };
    app.bag.forEach(it -> {
      if (it instanceof Route.Definition) {
        this.bag.add(rewrite.apply((Definition) it));
      } else if (it instanceof MvcClass) {
        Object routes = path.<Object>map(p -> new MvcClass(((MvcClass) it).routeClass, p, prefix))
            .orElse(it);
        this.bag.add(routes);
      } else {
        this.bag.add(it);
      }
    });
    app.onStart.forEach(this.onStart::add);
    app.onStarted.forEach(this.onStarted::add);
    app.onStop.forEach(this.onStop::add);
    if (app.mapper != null) {
      this.map(app.mapper);
    }
    if (apprefs == null) {
      apprefs = new ArrayList<>();
    }
    apprefs.add(app);
    return this;
  }
  public Jooby env(final Env.Builder env) {
    this.env = requireNonNull(env, "Env builder is required.");
    return this;
  }
  @Override
  public Jooby onStart(final Throwing.Runnable callback) {
    LifeCycle.super.onStart(callback);
    return this;
  }
  @Override
  public Jooby onStart(final Throwing.Consumer<Registry> callback) {
    requireNonNull(callback, "Callback is required.");
    onStart.add(callback);
    return this;
  }
  @Override
  public Jooby onStarted(final Throwing.Runnable callback) {
    LifeCycle.super.onStarted(callback);
    return this;
  }
  @Override
  public Jooby onStarted(final Throwing.Consumer<Registry> callback) {
    requireNonNull(callback, "Callback is required.");
    onStarted.add(callback);
    return this;
  }
  @Override
  public Jooby onStop(final Throwing.Runnable callback) {
    LifeCycle.super.onStop(callback);
    return this;
  }
  @Override
  public Jooby onStop(final Throwing.Consumer<Registry> callback) {
    requireNonNull(callback, "Callback is required.");
    onStop.add(callback);
    return this;
  }
  public EnvPredicate on(final String env, final Runnable callback) {
    requireNonNull(env, "Env is required.");
    return on(envpredicate(env), callback);
  }
  public EnvPredicate on(final String env, final Consumer<Config> callback) {
    requireNonNull(env, "Env is required.");
    return on(envpredicate(env), callback);
  }
  public EnvPredicate on(final Predicate<String> predicate, final Runnable callback) {
    requireNonNull(predicate, "Predicate is required.");
    requireNonNull(callback, "Callback is required.");
    return on(predicate, conf -> callback.run());
  }
  public EnvPredicate on(final Predicate<String> predicate, final Consumer<Config> callback) {
    requireNonNull(predicate, "Predicate is required.");
    requireNonNull(callback, "Callback is required.");
    this.bag.add(new EnvDep(predicate, callback));
    return otherwise -> this.bag.add(new EnvDep(predicate.negate(), otherwise));
  }
  public Jooby on(final String env1, final String env2, final String env3,
      final Runnable callback) {
    on(envpredicate(env1).or(envpredicate(env2)).or(envpredicate(env3)), callback);
    return this;
  }
  @Override
  public <T> T require(final Key<T> type) {
    checkState(injector != null,
        "Registry is not ready. Require calls are available at application startup time, see http:
    try {
      return injector.getInstance(type);
    } catch (ProvisionException x) {
      Throwable cause = x.getCause();
      if (cause instanceof Err) {
        throw (Err) cause;
      }
      throw x;
    }
  }
  @Override
  public Route.OneArgHandler promise(final Deferred.Initializer initializer) {
    return req -> {
      return new Deferred(initializer);
    };
  }
  @Override
  public Route.OneArgHandler promise(final String executor,
      final Deferred.Initializer initializer) {
    return req -> new Deferred(executor, initializer);
  }
  @Override
  public Route.OneArgHandler promise(final Deferred.Initializer0 initializer) {
    return req -> {
      return new Deferred(initializer);
    };
  }
  @Override
  public Route.OneArgHandler promise(final String executor,
      final Deferred.Initializer0 initializer) {
    return req -> new Deferred(executor, initializer);
  }
  public Session.Definition session(final Class<? extends Session.Store> store) {
    this.session = new Session.Definition(requireNonNull(store, "A session store is required."));
    return this.session;
  }
  public Session.Definition cookieSession() {
    this.session = new Session.Definition();
    return this.session;
  }
  public Session.Definition session(final Session.Store store) {
    this.session = new Session.Definition(requireNonNull(store, "A session store is required."));
    return this.session;
  }
  public Jooby parser(final Parser parser) {
    if (parser instanceof BeanParser) {
      beanParser = Optional.of(parser);
    } else {
      bag.add(requireNonNull(parser, "A parser is required."));
    }
    return this;
  }
  public Jooby renderer(final Renderer renderer) {
    this.bag.add(requireNonNull(renderer, "A renderer is required."));
    return this;
  }
  @Override
  public Route.Definition before(final String method, final String pattern,
      final Route.Before handler) {
    return appendDefinition(method, pattern, handler);
  }
  @Override
  public Route.Definition after(final String method, final String pattern,
      final Route.After handler) {
    return appendDefinition(method, pattern, handler);
  }
  @Override
  public Route.Definition complete(final String method, final String pattern,
      final Route.Complete handler) {
    return appendDefinition(method, pattern, handler);
  }
  @Override
  public Route.Definition use(final String path, final Route.Filter filter) {
    return appendDefinition("*", path, filter);
  }
  @Override
  public Route.Definition use(final String verb, final String path, final Route.Filter filter) {
    return appendDefinition(verb, path, filter);
  }
  @Override
  public Route.Definition use(final String verb, final String path, final Route.Handler handler) {
    return appendDefinition(verb, path, handler);
  }
  @Override
  public Route.Definition use(final String path, final Route.Handler handler) {
    return appendDefinition("*", path, handler);
  }
  @Override
  public Route.Definition use(final String path, final Route.OneArgHandler handler) {
    return appendDefinition("*", path, handler);
  }
  @Override
  public Route.Definition get(final String path, final Route.Handler handler) {
    if (handler instanceof AssetHandler) {
      return assets(path, (AssetHandler) handler);
    } else {
      return appendDefinition(GET, path, handler);
    }
  }
  @Override
  public Route.Collection get(final String path1, final String path2, final Route.Handler handler) {
    return new Route.Collection(
        new Route.Definition[]{get(path1, handler), get(path2, handler)});
  }
  @Override
  public Route.Collection get(final String path1, final String path2, final String path3,
      final Route.Handler handler) {
    return new Route.Collection(
        new Route.Definition[]{get(path1, handler), get(path2, handler), get(path3, handler)});
  }
  @Override
  public Route.Definition get(final String path, final Route.OneArgHandler handler) {
    return appendDefinition(GET, path, handler);
  }
  @Override
  public Route.Collection get(final String path1, final String path2,
      final Route.OneArgHandler handler) {
    return new Route.Collection(
        new Route.Definition[]{get(path1, handler), get(path2, handler)});
  }
  @Override
  public Route.Collection get(final String path1, final String path2,
      final String path3, final Route.OneArgHandler handler) {
    return new Route.Collection(
        new Route.Definition[]{get(path1, handler), get(path2, handler), get(path3, handler)});
  }
  @Override
  public Route.Definition get(final String path, final Route.ZeroArgHandler handler) {
    return appendDefinition(GET, path, handler);
  }
  @Override
  public Route.Collection get(final String path1, final String path2,
      final Route.ZeroArgHandler handler) {
    return new Route.Collection(
        new Route.Definition[]{get(path1, handler), get(path2, handler)});
  }
  @Override
  public Route.Collection get(final String path1, final String path2,
      final String path3, final Route.ZeroArgHandler handler) {
    return new Route.Collection(
        new Route.Definition[]{get(path1, handler), get(path2, handler), get(path3, handler)});
  }
  @Override
  public Route.Definition get(final String path, final Route.Filter filter) {
    return appendDefinition(GET, path, filter);
  }
  @Override
  public Route.Collection get(final String path1, final String path2, final Route.Filter filter) {
    return new Route.Collection(new Route.Definition[]{get(path1, filter), get(path2, filter)});
  }
  @Override
  public Route.Collection get(final String path1, final String path2,
      final String path3, final Route.Filter filter) {
    return new Route.Collection(
        new Route.Definition[]{get(path1, filter), get(path2, filter), get(path3, filter)});
  }
  @Override
  public Route.Definition post(final String path, final Route.Handler handler) {
    return appendDefinition(POST, path, handler);
  }
  @Override
  public Route.Collection post(final String path1, final String path2,
      final Route.Handler handler) {
    return new Route.Collection(
        new Route.Definition[]{post(path1, handler), post(path2, handler)});
  }
  @Override
  public Route.Collection post(final String path1, final String path2,
      final String path3, final Route.Handler handler) {
    return new Route.Collection(
        new Route.Definition[]{post(path1, handler), post(path2, handler), post(path3, handler)});
  }
  @Override
  public Route.Definition post(final String path, final Route.OneArgHandler handler) {
    return appendDefinition(POST, path, handler);
  }
  @Override
  public Route.Collection post(final String path1, final String path2,
      final Route.OneArgHandler handler) {
    return new Route.Collection(
        new Route.Definition[]{post(path1, handler), post(path2, handler)});
  }
  @Override
  public Route.Collection post(final String path1, final String path2,
      final String path3, final Route.OneArgHandler handler) {
    return new Route.Collection(
        new Route.Definition[]{post(path1, handler), post(path2, handler), post(path3, handler)});
  }
  @Override
  public Route.Definition post(final String path, final Route.ZeroArgHandler handler) {
    return appendDefinition(POST, path, handler);
  }
  @Override
  public Route.Collection post(final String path1, final String path2,
      final Route.ZeroArgHandler handler) {
    return new Route.Collection(
        new Route.Definition[]{post(path1, handler), post(path2, handler)});
  }
  @Override
  public Route.Collection post(final String path1, final String path2,
      final String path3, final Route.ZeroArgHandler handler) {
    return new Route.Collection(
        new Route.Definition[]{post(path1, handler), post(path2, handler), post(path3, handler)});
  }
  @Override
  public Route.Definition post(final String path, final Route.Filter filter) {
    return appendDefinition(POST, path, filter);
  }
  @Override
  public Route.Collection post(final String path1, final String path2,
      final Route.Filter filter) {
    return new Route.Collection(
        new Route.Definition[]{post(path1, filter), post(path2, filter)});
  }
  @Override
  public Route.Collection post(final String path1, final String path2,
      final String path3, final Route.Filter filter) {
    return new Route.Collection(
        new Route.Definition[]{post(path1, filter), post(path2, filter), post(path3, filter)});
  }
  @Override
  public Route.Definition head(final String path, final Route.Handler handler) {
    return appendDefinition(HEAD, path, handler);
  }
  @Override
  public Route.Definition head(final String path,
      final Route.OneArgHandler handler) {
    return appendDefinition(HEAD, path, handler);
  }
  @Override
  public Route.Definition head(final String path, final Route.ZeroArgHandler handler) {
    return appendDefinition(HEAD, path, handler);
  }
  @Override
  public Route.Definition head(final String path, final Route.Filter filter) {
    return appendDefinition(HEAD, path, filter);
  }
  @Override
  public Route.Definition head() {
    return appendDefinition(HEAD, "*", filter(HeadHandler.class)).name("*.head");
  }
  @Override
  public Route.Definition options(final String path, final Route.Handler handler) {
    return appendDefinition(OPTIONS, path, handler);
  }
  @Override
  public Route.Definition options(final String path,
      final Route.OneArgHandler handler) {
    return appendDefinition(OPTIONS, path, handler);
  }
  @Override
  public Route.Definition options(final String path,
      final Route.ZeroArgHandler handler) {
    return appendDefinition(OPTIONS, path, handler);
  }
  @Override
  public Route.Definition options(final String path,
      final Route.Filter filter) {
    return appendDefinition(OPTIONS, path, filter);
  }
  @Override
  public Route.Definition options() {
    return appendDefinition(OPTIONS, "*", handler(OptionsHandler.class)).name("*.options");
  }
  @Override
  public Route.Definition put(final String path,
      final Route.Handler handler) {
    return appendDefinition(PUT, path, handler);
  }
  @Override
  public Route.Collection put(final String path1, final String path2,
      final Route.Handler handler) {
    return new Route.Collection(
        new Route.Definition[]{put(path1, handler), put(path2, handler)});
  }
  @Override
  public Route.Collection put(final String path1, final String path2,
      final String path3, final Route.Handler handler) {
    return new Route.Collection(
        new Route.Definition[]{put(path1, handler), put(path2, handler), put(path3, handler)});
  }
  @Override
  public Route.Definition put(final String path,
      final Route.OneArgHandler handler) {
    return appendDefinition(PUT, path, handler);
  }
  @Override
  public Route.Collection put(final String path1, final String path2,
      final Route.OneArgHandler handler) {
    return new Route.Collection(
        new Route.Definition[]{put(path1, handler), put(path2, handler)});
  }
  @Override
  public Route.Collection put(final String path1, final String path2,
      final String path3, final Route.OneArgHandler handler) {
    return new Route.Collection(
        new Route.Definition[]{put(path1, handler), put(path2, handler), put(path3, handler)});
  }
  @Override
  public Route.Definition put(final String path,
      final Route.ZeroArgHandler handler) {
    return appendDefinition(PUT, path, handler);
  }
  @Override
  public Route.Collection put(final String path1, final String path2,
      final Route.ZeroArgHandler handler) {
    return new Route.Collection(
        new Route.Definition[]{put(path1, handler), put(path2, handler)});
  }
  @Override
  public Route.Collection put(final String path1, final String path2,
      final String path3, final Route.ZeroArgHandler handler) {
    return new Route.Collection(
        new Route.Definition[]{put(path1, handler), put(path2, handler), put(path3, handler)});
  }
  @Override
  public Route.Definition put(final String path,
      final Route.Filter filter) {
    return appendDefinition(PUT, path, filter);
  }
  @Override
  public Route.Collection put(final String path1, final String path2,
      final Route.Filter filter) {
    return new Route.Collection(
        new Route.Definition[]{put(path1, filter), put(path2, filter)});
  }
  @Override
  public Route.Collection put(final String path1, final String path2,
      final String path3, final Route.Filter filter) {
    return new Route.Collection(
        new Route.Definition[]{put(path1, filter), put(path2, filter), put(path3, filter)});
  }
  @Override
  public Route.Definition patch(final String path, final Route.Handler handler) {
    return appendDefinition(PATCH, path, handler);
  }
  @Override
  public Route.Collection patch(final String path1, final String path2,
      final Route.Handler handler) {
    return new Route.Collection(
        new Route.Definition[]{patch(path1, handler), patch(path2, handler)});
  }
  @Override
  public Route.Collection patch(final String path1, final String path2,
      final String path3, final Route.Handler handler) {
    return new Route.Collection(
        new Route.Definition[]{patch(path1, handler), patch(path2, handler),
            patch(path3, handler)});
  }
  @Override
  public Route.Definition patch(final String path, final Route.OneArgHandler handler) {
    return appendDefinition(PATCH, path, handler);
  }
  @Override
  public Route.Collection patch(final String path1, final String path2,
      final Route.OneArgHandler handler) {
    return new Route.Collection(
        new Route.Definition[]{patch(path1, handler), patch(path2, handler)});
  }
  @Override
  public Route.Collection patch(final String path1, final String path2,
      final String path3, final Route.OneArgHandler handler) {
    return new Route.Collection(
        new Route.Definition[]{patch(path1, handler), patch(path2, handler),
            patch(path3, handler)});
  }
  @Override
  public Route.Definition patch(final String path, final Route.ZeroArgHandler handler) {
    return appendDefinition(PATCH, path, handler);
  }
  @Override
  public Route.Collection patch(final String path1, final String path2,
      final Route.ZeroArgHandler handler) {
    return new Route.Collection(
        new Route.Definition[]{patch(path1, handler), patch(path2, handler)});
  }
  @Override
  public Route.Collection patch(final String path1, final String path2,
      final String path3, final Route.ZeroArgHandler handler) {
    return new Route.Collection(
        new Route.Definition[]{patch(path1, handler), patch(path2, handler),
            patch(path3, handler)});
  }
  @Override
  public Route.Definition patch(final String path,
      final Route.Filter filter) {
    return appendDefinition(PATCH, path, filter);
  }
  @Override
  public Route.Collection patch(final String path1, final String path2,
      final Route.Filter filter) {
    return new Route.Collection(
        new Route.Definition[]{patch(path1, filter), patch(path2, filter)});
  }
  @Override
  public Route.Collection patch(final String path1, final String path2,
      final String path3, final Route.Filter filter) {
    return new Route.Collection(
        new Route.Definition[]{patch(path1, filter), patch(path2, filter),
            patch(path3, filter)});
  }
  @Override
  public Route.Definition delete(final String path, final Route.Handler handler) {
    return appendDefinition(DELETE, path, handler);
  }
  @Override
  public Route.Collection delete(final String path1, final String path2,
      final Route.Handler handler) {
    return new Route.Collection(
        new Route.Definition[]{delete(path1, handler), delete(path2, handler)});
  }
  @Override
  public Route.Collection delete(final String path1, final String path2, final String path3,
      final Route.Handler handler) {
    return new Route.Collection(
        new Route.Definition[]{delete(path1, handler), delete(path2, handler),
            delete(path3, handler)});
  }
  @Override
  public Route.Definition delete(final String path, final Route.OneArgHandler handler) {
    return appendDefinition(DELETE, path, handler);
  }
  @Override
  public Route.Collection delete(final String path1, final String path2,
      final Route.OneArgHandler handler) {
    return new Route.Collection(
        new Route.Definition[]{delete(path1, handler), delete(path2, handler)});
  }
  @Override
  public Route.Collection delete(final String path1, final String path2, final String path3,
      final Route.OneArgHandler handler) {
    return new Route.Collection(
        new Route.Definition[]{delete(path1, handler), delete(path2, handler),
            delete(path3, handler)});
  }
  @Override
  public Route.Definition delete(final String path,
      final Route.ZeroArgHandler handler) {
    return appendDefinition(DELETE, path, handler);
  }
  @Override
  public Route.Collection delete(final String path1,
      final String path2, final Route.ZeroArgHandler handler) {
    return new Route.Collection(
        new Route.Definition[]{delete(path1, handler), delete(path2, handler)});
  }
  @Override
  public Route.Collection delete(final String path1, final String path2, final String path3,
      final Route.ZeroArgHandler handler) {
    return new Route.Collection(
        new Route.Definition[]{delete(path1, handler), delete(path2, handler),
            delete(path3, handler)});
  }
  @Override
  public Route.Definition delete(final String path, final Route.Filter filter) {
    return appendDefinition(DELETE, path, filter);
  }
  @Override
  public Route.Collection delete(final String path1, final String path2,
      final Route.Filter filter) {
    return new Route.Collection(
        new Route.Definition[]{delete(path1, filter), delete(path2, filter)});
  }
  @Override
  public Route.Collection delete(final String path1, final String path2, final String path3,
      final Route.Filter filter) {
    return new Route.Collection(
        new Route.Definition[]{delete(path1, filter), delete(path2, filter),
            delete(path3, filter)});
  }
  @Override
  public Route.Definition trace(final String path, final Route.Handler handler) {
    return appendDefinition(TRACE, path, handler);
  }
  @Override
  public Route.Definition trace(final String path, final Route.OneArgHandler handler) {
    return appendDefinition(TRACE, path, handler);
  }
  @Override
  public Route.Definition trace(final String path, final Route.ZeroArgHandler handler) {
    return appendDefinition(TRACE, path, handler);
  }
  @Override
  public Route.Definition trace(final String path, final Route.Filter filter) {
    return appendDefinition(TRACE, path, filter);
  }
  @Override
  public Route.Definition trace() {
    return appendDefinition(TRACE, "*", handler(TraceHandler.class)).name("*.trace");
  }
  @Override
  public Route.Definition connect(final String path, final Route.Handler handler) {
    return appendDefinition(CONNECT, path, handler);
  }
  @Override
  public Route.Definition connect(final String path, final Route.OneArgHandler handler) {
    return appendDefinition(CONNECT, path, handler);
  }
  @Override
  public Route.Definition connect(final String path, final Route.ZeroArgHandler handler) {
    return appendDefinition(CONNECT, path, handler);
  }
  @Override
  public Route.Definition connect(final String path, final Route.Filter filter) {
    return appendDefinition(CONNECT, path, filter);
  }
  private Route.Handler handler(final Class<? extends Route.Handler> handler) {
    requireNonNull(handler, "Route handler is required.");
    return (req, rsp) -> req.require(handler).handle(req, rsp);
  }
  private Route.Filter filter(final Class<? extends Route.Filter> filter) {
    requireNonNull(filter, "Filter is required.");
    return (req, rsp, chain) -> req.require(filter).handle(req, rsp, chain);
  }
  @Override
  public Route.AssetDefinition assets(final String path, final Path basedir) {
    return assets(path, new AssetHandler(basedir));
  }
  @Override
  public Route.AssetDefinition assets(final String path, final String location) {
    return assets(path, new AssetHandler(location));
  }
  @Override
  public Route.AssetDefinition assets(final String path, final AssetHandler handler) {
    Route.AssetDefinition route = appendDefinition(GET, path, handler, Route.AssetDefinition::new);
    return configureAssetHandler(route);
  }
  @Override
  public Route.Collection use(final Class<?> routeClass) {
    return use("", routeClass);
  }
  @Override
  public Route.Collection use(final String path, final Class<?> routeClass) {
    requireNonNull(routeClass, "Route class is required.");
    requireNonNull(path, "Path is required");
    MvcClass mvc = new MvcClass(routeClass, path, prefix);
    bag.add(mvc);
    return new Route.Collection(mvc);
  }
  private Route.Definition appendDefinition(String method, String pattern, Route.Filter filter) {
    return appendDefinition(method, pattern, filter, Route.Definition::new);
  }
  private <T extends Route.Definition> T appendDefinition(String method, String pattern,
      Route.Filter filter, Throwing.Function4<String, String, Route.Filter, Boolean, T> creator) {
    String pathPattern = prefixPath(pattern).orElse(pattern);
    T route = creator.apply(method, pathPattern, filter, caseSensitiveRouting);
    if (prefix != null) {
      route.prefix = prefix;
      route.name(route.name());
    }
    bag.add(route);
    return route;
  }
  public Jooby use(final Jooby.Module module) {
    requireNonNull(module, "A module is required.");
    bag.add(module);
    return this;
  }
  public Jooby conf(final String path) {
    this.confname = path;
    use(ConfigFactory.parseResources(path));
    return this;
  }
  public Jooby conf(final File path) {
    this.confname = path.getName();
    use(ConfigFactory.parseFile(path));
    return this;
  }
  public Jooby use(final Config config) {
    this.srcconf = requireNonNull(config, "Config required.");
    return this;
  }
  @Override
  public Jooby err(final Err.Handler err) {
    this.bag.add(requireNonNull(err, "An err handler is required."));
    return this;
  }
  @Override
  public WebSocket.Definition ws(final String path, final WebSocket.OnOpen handler) {
    WebSocket.Definition ws = new WebSocket.Definition(path, handler);
    checkArgument(bag.add(ws), "Duplicated path: '%s'", path);
    return ws;
  }
  @Override
  public <T> WebSocket.Definition ws(final String path,
      final Class<? extends WebSocket.OnMessage<T>> handler) {
    String fpath = Optional.ofNullable(handler.getAnnotation(org.jooby.mvc.Path.class))
        .map(it -> path + "/" + it.value()[0])
        .orElse(path);
    WebSocket.Definition ws = ws(fpath, MvcWebSocket.newWebSocket(handler));
    Optional.ofNullable(handler.getAnnotation(Consumes.class))
        .ifPresent(consumes -> Arrays.asList(consumes.value()).forEach(ws::consumes));
    Optional.ofNullable(handler.getAnnotation(Produces.class))
        .ifPresent(produces -> Arrays.asList(produces.value()).forEach(ws::produces));
    return ws;
  }
  @Override
  public Route.Definition sse(final String path, final Sse.Handler handler) {
    return appendDefinition(GET, path, handler).consumes(MediaType.sse);
  }
  @Override
  public Route.Definition sse(final String path, final Sse.Handler1 handler) {
    return appendDefinition(GET, path, handler).consumes(MediaType.sse);
  }
  @SuppressWarnings("rawtypes")
  @Override
  public Route.Collection with(final Runnable callback) {
    int size = this.bag.size();
    callback.run();
    List<Route.Props> local = this.bag.stream()
        .skip(size)
        .filter(Route.Props.class::isInstance)
        .map(Route.Props.class::cast)
        .collect(Collectors.toList());
    return new Route.Collection(local.toArray(new Route.Props[local.size()]));
  }
  public static void run(final Supplier<? extends Jooby> app, final String... args) {
    Config conf = ConfigFactory.systemProperties()
        .withFallback(args(args));
    System.setProperty("logback.configurationFile", logback(conf));
    app.get().start(args);
  }
  public static void run(final Class<? extends Jooby> app, final String... args) {
    run(() -> Try.apply(() -> app.newInstance()).get(), args);
  }
  public static Config exportConf(final Jooby app) {
    AtomicReference<Config> conf = new AtomicReference<>(ConfigFactory.empty());
    app.on("*", c -> {
      conf.set(c);
    });
    exportRoutes(app);
    return conf.get();
  }
  public static List<Definition> exportRoutes(final Jooby app) {
    @SuppressWarnings("serial") class Success extends RuntimeException {
      List<Definition> routes;
      Success(final List<Route.Definition> routes) {
        this.routes = routes;
      }
    }
    List<Definition> routes = Collections.emptyList();
    try {
      app.start(new String[0], r -> {
        throw new Success(r);
      });
    } catch (Success success) {
      routes = success.routes;
    } catch (Throwable x) {
      logger(app).debug("Failed bootstrap: {}", app, x);
    }
    return routes;
  }
  public void start() {
    start(new String[0]);
  }
  public void start(final String... args) {
    try {
      start(args, null);
    } catch (Throwable x) {
      stop();
      String msg = "An error occurred while starting the application:";
      if (throwBootstrapException) {
        throw new Err(Status.SERVICE_UNAVAILABLE, msg, x);
      } else {
        logger(this).error(msg, x);
      }
    }
  }
  @SuppressWarnings("unchecked")
  private void start(final String[] args, final Consumer<List<Route.Definition>> routes)
      throws Throwable {
    long start = System.currentTimeMillis();
    started.set(true);
    this.injector = bootstrap(args(args), routes);
    Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    Config conf = injector.getInstance(Config.class);
    Logger log = logger(this);
    injector.injectMembers(this);
    if (conf.hasPath("jooby.internal.onStart")) {
      ClassLoader loader = getClass().getClassLoader();
      Object internalOnStart = loader.loadClass(conf.getString("jooby.internal.onStart"))
          .newInstance();
      onStart.add((Throwing.Consumer<Registry>) internalOnStart);
    }
    for (Throwing.Consumer<Registry> onStart : this.onStart) {
      onStart.accept(this);
    }
    Set<Route.Definition> routeDefs = injector.getInstance(Route.KEY);
    Set<WebSocket.Definition> sockets = injector.getInstance(WebSocket.KEY);
    if (mapper != null) {
      routeDefs.forEach(it -> it.map(mapper));
    }
    AppPrinter printer = new AppPrinter(routeDefs, sockets, conf);
    printer.printConf(log, conf);
    Server server = injector.getInstance(Server.class);
    String serverName = server.getClass().getSimpleName().replace("Server", "").toLowerCase();
    server.start();
    long end = System.currentTimeMillis();
    log.info("[{}@{}]: Server started in {}ms\n\n{}\n",
        conf.getString("application.env"),
        serverName,
        end - start,
        printer);
    for (Throwing.Consumer<Registry> onStarted : this.onStarted) {
      onStarted.accept(this);
    }
    boolean join = conf.hasPath("server.join") ? conf.getBoolean("server.join") : true;
    if (join) {
      server.join();
    }
  }
  @Override
  @SuppressWarnings("unchecked")
  public Jooby map(final Mapper<?> mapper) {
    requireNonNull(mapper, "Mapper is required.");
    if (mappers.add(mapper.name())) {
      this.mapper = Optional.ofNullable(this.mapper)
          .map(next -> Route.Mapper.chain(mapper, next))
          .orElse((Mapper<Object>) mapper);
    }
    return this;
  }
  public Jooby injector(
      final BiFunction<Stage, com.google.inject.Module, Injector> injectorFactory) {
    this.injectorFactory = injectorFactory;
    return this;
  }
  public <T> Jooby bind(final Class<T> type, final Class<? extends T> implementation) {
    use((env, conf, binder) -> {
      binder.bind(type).to(implementation);
    });
    return this;
  }
  public <T> Jooby bind(final Class<T> type, final Supplier<T> implementation) {
    use((env, conf, binder) -> {
      binder.bind(type).toInstance(implementation.get());
    });
    return this;
  }
  public <T> Jooby bind(final Class<T> type) {
    use((env, conf, binder) -> {
      binder.bind(type);
    });
    return this;
  }
  @SuppressWarnings({"rawtypes", "unchecked"})
  public Jooby bind(final Object service) {
    use((env, conf, binder) -> {
      Class type = service.getClass();
      binder.bind(type).toInstance(service);
    });
    return this;
  }
  public <T> Jooby bind(final Class<T> type, final Function<Config, ? extends T> provider) {
    use((env, conf, binder) -> {
      T service = provider.apply(conf);
      binder.bind(type).toInstance(service);
    });
    return this;
  }
  @SuppressWarnings({"unchecked", "rawtypes"})
  public <T> Jooby bind(final Function<Config, T> provider) {
    use((env, conf, binder) -> {
      Object service = provider.apply(conf);
      Class type = service.getClass();
      binder.bind(type).toInstance(service);
    });
    return this;
  }
  public Jooby dateFormat(final String dateFormat) {
    this.dateFormat = requireNonNull(dateFormat, "DateFormat required.");
    return this;
  }
  public Jooby numberFormat(final String numberFormat) {
    this.numberFormat = requireNonNull(numberFormat, "NumberFormat required.");
    return this;
  }
  public Jooby charset(final Charset charset) {
    this.charset = requireNonNull(charset, "Charset required.");
    return this;
  }
  public Jooby lang(final String... languages) {
    this.languages = languages;
    return this;
  }
  public Jooby timezone(final ZoneId zoneId) {
    this.zoneId = requireNonNull(zoneId, "ZoneId required.");
    return this;
  }
  public Jooby port(final int port) {
    this.port = port;
    return this;
  }
  public Jooby securePort(final int port) {
    this.securePort = port;
    return this;
  }
  public Jooby http2() {
    this.http2 = true;
    return this;
  }
  public Jooby executor(final ExecutorService executor) {
    executor((Executor) executor);
    onStop(r -> executor.shutdown());
    return this;
  }
  public Jooby executor(final Executor executor) {
    this.defaultExecSet = true;
    this.executors.add(binder -> {
      binder.bind(Key.get(String.class, Names.named("deferred"))).toInstance("deferred");
      binder.bind(Key.get(Executor.class, Names.named("deferred"))).toInstance(executor);
    });
    return this;
  }
  public Jooby executor(final String name, final ExecutorService executor) {
    executor(name, (Executor) executor);
    onStop(r -> executor.shutdown());
    return this;
  }
  public Jooby executor(final String name, final Executor executor) {
    this.executors.add(binder -> {
      binder.bind(Key.get(Executor.class, Names.named(name))).toInstance(executor);
    });
    return this;
  }
  public Jooby executor(final String name) {
    defaultExecSet = true;
    this.executors.add(binder -> {
      binder.bind(Key.get(String.class, Names.named("deferred"))).toInstance(name);
    });
    return this;
  }
  private Jooby executor(final String name, final Class<? extends Provider<Executor>> provider) {
    this.executors.add(binder -> {
      binder.bind(Key.get(Executor.class, Names.named(name))).toProvider(provider)
          .in(Singleton.class);
    });
    return this;
  }
  public Jooby throwBootstrapException() {
    this.throwBootstrapException = true;
    return this;
  }
  public Jooby caseSensitiveRouting(boolean enabled) {
    this.caseSensitiveRouting = enabled;
    return this;
  }
  private static List<Object> normalize(final List<Object> services, final Env env,
      final RouteMetadata classInfo, final boolean caseSensitiveRouting) {
    List<Object> result = new ArrayList<>();
    List<Object> snapshot = services;
    snapshot.forEach(candidate -> {
      if (candidate instanceof Route.Definition) {
        result.add(candidate);
      } else if (candidate instanceof MvcClass) {
        MvcClass mvcRoute = ((MvcClass) candidate);
        Class<?> mvcClass = mvcRoute.routeClass;
        String path = ((MvcClass) candidate).path;
        MvcRoutes.routes(env, classInfo, path, caseSensitiveRouting, mvcClass)
            .forEach(route -> result.add(mvcRoute.apply(route)));
      } else {
        result.add(candidate);
      }
    });
    return result;
  }
  private static List<Object> processEnvDep(final Set<Object> src, final Env env) {
    List<Object> result = new ArrayList<>();
    List<Object> bag = new ArrayList<>(src);
    bag.forEach(it -> {
      if (it instanceof EnvDep) {
        EnvDep envdep = (EnvDep) it;
        if (envdep.predicate.test(env.name())) {
          int from = src.size();
          envdep.callback.accept(env.config());
          int to = src.size();
          result.addAll(new ArrayList<>(src).subList(from, to));
        }
      } else {
        result.add(it);
      }
    });
    return result;
  }
  private Injector bootstrap(final Config args,
      final Consumer<List<Route.Definition>> rcallback) throws Throwable {
    Config initconf = Optional.ofNullable(srcconf)
        .orElseGet(() -> ConfigFactory.parseResources("application.conf"));
    List<Config> modconf = modconf(this.bag);
    Config conf = buildConfig(initconf, args, modconf);
    final List<Locale> locales = LocaleUtils.parse(conf.getString("application.lang"));
    Env env = this.env.build(conf, this, locales.get(0));
    String envname = env.name();
    final Charset charset = Charset.forName(conf.getString("application.charset"));
    String dateFormat = conf.getString("application.dateFormat");
    ZoneId zoneId = ZoneId.of(conf.getString("application.tz"));
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter
        .ofPattern(dateFormat, locales.get(0))
        .withZone(zoneId);
    DateTimeFormatter zonedDateTimeFormat = DateTimeFormatter
        .ofPattern(conf.getString("application.zonedDateTimeFormat"));
    DecimalFormat numberFormat = new DecimalFormat(conf.getString("application.numberFormat"));
    Stage stage = "dev".equals(envname) ? Stage.DEVELOPMENT : Stage.PRODUCTION;
    RouteMetadata rm = new RouteMetadata(env);
    List<Object> realbag = processEnvDep(this.bag, env);
    List<Config> realmodconf = modconf(realbag);
    List<Object> bag = normalize(realbag, env, rm, caseSensitiveRouting);
    if (rcallback != null) {
      List<Route.Definition> routes = bag.stream()
          .filter(it -> it instanceof Route.Definition)
          .map(it -> (Route.Definition) it)
          .collect(Collectors.<Route.Definition>toList());
      rcallback.accept(routes);
    }
    Config finalConfig;
    Env finalEnv;
    if (modconf.size() != realmodconf.size()) {
      finalConfig = buildConfig(initconf, args, realmodconf);
      finalEnv = this.env.build(finalConfig, this, locales.get(0));
    } else {
      finalConfig = conf;
      finalEnv = env;
    }
    boolean cookieSession = session.store() == null;
    if (cookieSession && !finalConfig.hasPath("application.secret")) {
      throw new IllegalStateException("Required property 'application.secret' is missing");
    }
    if (!defaultExecSet) {
      executor(MoreExecutors.directExecutor());
    }
    executor("direct", MoreExecutors.directExecutor());
    executor("server", ServerExecutorProvider.class);
    xss(finalEnv);
    @SuppressWarnings("unchecked")
    com.google.inject.Module joobyModule = binder -> {
      new TypeConverters().configure(binder);
      bindConfig(binder, finalConfig);
      binder.bind(Env.class).toInstance(finalEnv);
      binder.bind(Charset.class).toInstance(charset);
      binder.bind(Locale.class).toInstance(locales.get(0));
      TypeLiteral<List<Locale>> localeType = (TypeLiteral<List<Locale>>) TypeLiteral
          .get(Types.listOf(Locale.class));
      binder.bind(localeType).toInstance(locales);
      binder.bind(ZoneId.class).toInstance(zoneId);
      binder.bind(TimeZone.class).toInstance(TimeZone.getTimeZone(zoneId));
      binder.bind(DateTimeFormatter.class).toInstance(dateTimeFormatter);
      binder.bind(NumberFormat.class).toInstance(numberFormat);
      binder.bind(DecimalFormat.class).toInstance(numberFormat);
      binder.bind(SSLContext.class).toProvider(SslContextProvider.class);
      Multibinder<Definition> definitions = Multibinder
          .newSetBinder(binder, Definition.class);
      Multibinder<WebSocket.Definition> sockets = Multibinder
          .newSetBinder(binder, WebSocket.Definition.class);
      File tmpdir = new File(finalConfig.getString("application.tmpdir"));
      tmpdir.mkdirs();
      binder.bind(File.class).annotatedWith(Names.named("application.tmpdir"))
          .toInstance(tmpdir);
      binder.bind(ParameterNameProvider.class).toInstance(rm);
      Multibinder<Err.Handler> ehandlers = Multibinder
          .newSetBinder(binder, Err.Handler.class);
      Multibinder<Parser> parsers = Multibinder
          .newSetBinder(binder, Parser.class);
      Multibinder<Renderer> renderers = Multibinder
          .newSetBinder(binder, Renderer.class);
      parsers.addBinding().toInstance(BuiltinParser.Basic);
      parsers.addBinding().toInstance(BuiltinParser.Collection);
      parsers.addBinding().toInstance(BuiltinParser.Optional);
      parsers.addBinding().toInstance(BuiltinParser.Enum);
      parsers.addBinding().toInstance(BuiltinParser.Bytes);
      renderers.addBinding().toInstance(BuiltinRenderer.asset);
      renderers.addBinding().toInstance(BuiltinRenderer.bytes);
      renderers.addBinding().toInstance(BuiltinRenderer.byteBuffer);
      renderers.addBinding().toInstance(BuiltinRenderer.file);
      renderers.addBinding().toInstance(BuiltinRenderer.charBuffer);
      renderers.addBinding().toInstance(BuiltinRenderer.stream);
      renderers.addBinding().toInstance(BuiltinRenderer.reader);
      renderers.addBinding().toInstance(BuiltinRenderer.fileChannel);
      Set<Object> routeClasses = new HashSet<>();
      for (Object it : bag) {
        Try.run(() -> bindService(
            logger(this),
            this.bag,
            finalConfig,
            finalEnv,
            rm,
            binder,
            definitions,
            sockets,
            ehandlers,
            parsers,
            renderers,
            routeClasses,
            caseSensitiveRouting)
            .accept(it))
            .throwException();
      }
      parsers.addBinding().toInstance(new DateParser(dateFormat));
      parsers.addBinding().toInstance(new LocalDateParser(dateTimeFormatter));
      parsers.addBinding().toInstance(new ZonedDateTimeParser(zonedDateTimeFormat));
      parsers.addBinding().toInstance(new LocaleParser());
      parsers.addBinding().toInstance(new StaticMethodParser("valueOf"));
      parsers.addBinding().toInstance(new StaticMethodParser("fromString"));
      parsers.addBinding().toInstance(new StaticMethodParser("forName"));
      parsers.addBinding().toInstance(new StringConstructorParser());
      parsers.addBinding().toInstance(beanParser.orElseGet(() -> new BeanParser(false)));
      binder.bind(ParserExecutor.class).in(Singleton.class);
      renderers.addBinding().toInstance(new DefaulErrRenderer());
      renderers.addBinding().toInstance(BuiltinRenderer.text);
      binder.bind(HttpHandler.class).to(HttpHandlerImpl.class).in(Singleton.class);
      RequestScope requestScope = new RequestScope();
      binder.bind(RequestScope.class).toInstance(requestScope);
      binder.bindScope(RequestScoped.class, requestScope);
      binder.bind(Session.Definition.class)
          .toProvider(session(finalConfig.getConfig("session"), session))
          .asEagerSingleton();
      Object sstore = session.store();
      if (cookieSession) {
        binder.bind(SessionManager.class).to(CookieSessionManager.class)
            .asEagerSingleton();
      } else {
        binder.bind(SessionManager.class).to(ServerSessionManager.class).asEagerSingleton();
        if (sstore instanceof Class) {
          binder.bind(Store.class).to((Class<? extends Store>) sstore)
              .asEagerSingleton();
        } else {
          binder.bind(Store.class).toInstance((Store) sstore);
        }
      }
      binder.bind(Request.class).toProvider(Providers.outOfScope(Request.class))
          .in(RequestScoped.class);
      binder.bind(Route.Chain.class).toProvider(Providers.outOfScope(Route.Chain.class))
          .in(RequestScoped.class);
      binder.bind(Response.class).toProvider(Providers.outOfScope(Response.class))
          .in(RequestScoped.class);
      binder.bind(Sse.class).toProvider(Providers.outOfScope(Sse.class))
          .in(RequestScoped.class);
      binder.bind(Session.class).toProvider(Providers.outOfScope(Session.class))
          .in(RequestScoped.class);
      ehandlers.addBinding().toInstance(new Err.DefHandler());
      executors.forEach(it -> it.accept(binder));
    };
    Injector injector = injectorFactory.apply(stage, joobyModule);
    if (apprefs != null) {
      apprefs.forEach(app -> app.injector = injector);
      apprefs.clear();
      apprefs = null;
    }
    onStart.addAll(0, finalEnv.startTasks());
    onStarted.addAll(0, finalEnv.startedTasks());
    onStop.addAll(finalEnv.stopTasks());
    this.bag.clear();
    this.bag = ImmutableSet.of();
    this.executors.clear();
    this.executors = ImmutableList.of();
    return injector;
  }
  private void xss(final Env env) {
    Escaper ufe = UrlEscapers.urlFragmentEscaper();
    Escaper fpe = UrlEscapers.urlFormParameterEscaper();
    Escaper pse = UrlEscapers.urlPathSegmentEscaper();
    Escaper html = HtmlEscapers.htmlEscaper();
    env.xss("urlFragment", ufe::escape)
        .xss("formParam", fpe::escape)
        .xss("pathSegment", pse::escape)
        .xss("html", html::escape);
  }
  private static Provider<Session.Definition> session(final Config $session,
      final Session.Definition session) {
    return () -> {
      session.saveInterval(session.saveInterval()
          .orElse($session.getDuration("saveInterval", TimeUnit.MILLISECONDS)));
      Cookie.Definition source = session.cookie();
      source.name(source.name()
          .orElse($session.getString("cookie.name")));
      if (!source.comment().isPresent() && $session.hasPath("cookie.comment")) {
        source.comment($session.getString("cookie.comment"));
      }
      if (!source.domain().isPresent() && $session.hasPath("cookie.domain")) {
        source.domain($session.getString("cookie.domain"));
      }
      source.httpOnly(source.httpOnly()
          .orElse($session.getBoolean("cookie.httpOnly")));
      Object maxAge = $session.getAnyRef("cookie.maxAge");
      if (maxAge instanceof String) {
        maxAge = $session.getDuration("cookie.maxAge", TimeUnit.SECONDS);
      }
      source.maxAge(source.maxAge()
          .orElse(((Number) maxAge).intValue()));
      source.path(source.path()
          .orElse($session.getString("cookie.path")));
      source.secure(source.secure()
          .orElse($session.getBoolean("cookie.secure")));
      return session;
    };
  }
  private static Throwing.Consumer<? super Object> bindService(Logger log,
      final Set<Object> src,
      final Config conf,
      final Env env,
      final RouteMetadata rm,
      final Binder binder,
      final Multibinder<Route.Definition> definitions,
      final Multibinder<WebSocket.Definition> sockets,
      final Multibinder<Err.Handler> ehandlers,
      final Multibinder<Parser> parsers,
      final Multibinder<Renderer> renderers,
      final Set<Object> routeClasses,
      final boolean caseSensitiveRouting) {
    return it -> {
      if (it instanceof Jooby.Module) {
        int from = src.size();
        install(log, (Jooby.Module) it, env, conf, binder);
        int to = src.size();
        if (to > from) {
          List<Object> elements = normalize(new ArrayList<>(src).subList(from, to), env, rm,
              caseSensitiveRouting);
          for (Object e : elements) {
            bindService(log, src,
                conf,
                env,
                rm,
                binder,
                definitions,
                sockets,
                ehandlers,
                parsers,
                renderers,
                routeClasses, caseSensitiveRouting).accept(e);
          }
        }
      } else if (it instanceof Route.Definition) {
        Route.Definition rdef = (Definition) it;
        Route.Filter h = rdef.filter();
        if (h instanceof Route.MethodHandler) {
          Class<?> routeClass = ((Route.MethodHandler) h).implementingClass();
          if (routeClasses.add(routeClass)) {
            binder.bind(routeClass);
          }
          definitions.addBinding().toInstance(rdef);
        } else {
          definitions.addBinding().toInstance(rdef);
        }
      } else if (it instanceof WebSocket.Definition) {
        sockets.addBinding().toInstance((WebSocket.Definition) it);
      } else if (it instanceof Parser) {
        parsers.addBinding().toInstance((Parser) it);
      } else if (it instanceof Renderer) {
        renderers.addBinding().toInstance((Renderer) it);
      } else {
        ehandlers.addBinding().toInstance((Err.Handler) it);
      }
    };
  }
  private static List<Config> modconf(final Collection<Object> bag) {
    return bag.stream()
        .filter(it -> it instanceof Jooby.Module)
        .map(it -> ((Jooby.Module) it).config())
        .filter(c -> !c.isEmpty())
        .collect(Collectors.toList());
  }
  public boolean isStarted() {
    return started.get();
  }
  public void stop() {
    if (started.compareAndSet(true, false)) {
      Logger log = logger(this);
      fireStop(this, log, onStop);
      if (injector != null) {
        try {
          injector.getInstance(Server.class).stop();
        } catch (Throwable ex) {
          log.debug("server.stop() resulted in exception", ex);
        }
      }
      injector = null;
      log.info("Stopped");
    }
  }
  private static void fireStop(final Jooby app, final Logger log,
      final List<Throwing.Consumer<Registry>> onStop) {
    onStop.forEach(c -> Try.run(() -> c.accept(app))
        .onFailure(x -> log.error("shutdown of {} resulted in error", c, x)));
  }
  private Config buildConfig(final Config source, final Config args,
      final List<Config> modules) {
    Config system = ConfigFactory.systemProperties();
    Config tmpdir = source.hasPath("java.io.tmpdir") ? source : system;
    system = system
        .withValue("file.encoding", fromAnyRef(System.getProperty("file.encoding")))
        .withValue("java.io.tmpdir",
            fromAnyRef(Paths.get(tmpdir.getString("java.io.tmpdir")).normalize().toString()));
    Config moduleStack = ConfigFactory.empty();
    for (Config module : ImmutableList.copyOf(modules).reverse()) {
      moduleStack = moduleStack.withFallback(module);
    }
    String env = Arrays.asList(system, args, source).stream()
        .filter(it -> it.hasPath("application.env"))
        .findFirst()
        .map(c -> c.getString("application.env"))
        .orElse("dev");
    String cpath = Arrays.asList(system, args, source).stream()
        .filter(it -> it.hasPath("application.path"))
        .findFirst()
        .map(c -> c.getString("application.path"))
        .orElse("/");
    Config envconf = envConf(source, env);
    Config conf = envconf.withFallback(source);
    return system
        .withFallback(args)
        .withFallback(conf)
        .withFallback(moduleStack)
        .withFallback(MediaType.types)
        .withFallback(defaultConfig(conf, Route.normalize(cpath)))
        .resolve();
  }
  static Config args(final String[] args) {
    if (args == null || args.length == 0) {
      return ConfigFactory.empty();
    }
    Map<String, String> conf = new HashMap<>();
    for (String arg : args) {
      String[] values = arg.split("=");
      String name;
      String value;
      if (values.length == 2) {
        name = values[0];
        value = values[1];
      } else {
        name = "application.env";
        value = values[0];
      }
      if (name.indexOf(".") == -1) {
        conf.put("application." + name, value);
      }
      conf.put(name, value);
    }
    return ConfigFactory.parseMap(conf, "args");
  }
  private Config envConf(final Config source, final String env) {
    String name = Optional.ofNullable(this.confname).orElse(source.origin().resource());
    Config result = ConfigFactory.empty();
    if (name != null) {
      int dot = name.lastIndexOf('.');
      name = name.substring(0, dot);
    } else {
      name = "application";
    }
    String envconfname = name + "." + env + ".conf";
    Config envconf = fileConfig(envconfname);
    Config appconf = fileConfig(name + ".conf");
    return result
        .withFallback(envconf)
        .withFallback(appconf)
        .withFallback(ConfigFactory.parseResources(envconfname));
  }
  static Config fileConfig(final String fname) {
    File dir = new File(System.getProperty("user.dir"));
    File froot = new File(dir, fname);
    if (froot.exists()) {
      return ConfigFactory.parseFile(froot);
    } else {
      File fconfig = new File(new File(dir, "conf"), fname);
      if (fconfig.exists()) {
        return ConfigFactory.parseFile(fconfig);
      }
    }
    return ConfigFactory.empty();
  }
  private Config defaultConfig(final Config conf, final String cpath) {
    String ns = Optional.ofNullable(getClass().getPackage())
        .map(Package::getName)
        .orElse("default." + getClass().getName());
    String[] parts = ns.split("\\.");
    String appname = parts[parts.length - 1];
    final List<Locale> locales;
    if (!conf.hasPath("application.lang")) {
      locales = Optional.ofNullable(this.languages)
          .map(langs -> LocaleUtils.parse(Joiner.on(",").join(langs)))
          .orElse(ImmutableList.of(Locale.getDefault()));
    } else {
      locales = LocaleUtils.parse(conf.getString("application.lang"));
    }
    Locale locale = locales.iterator().next();
    String lang = locale.toLanguageTag();
    final String tz;
    if (!conf.hasPath("application.tz")) {
      tz = Optional.ofNullable(zoneId).orElse(ZoneId.systemDefault()).getId();
    } else {
      tz = conf.getString("application.tz");
    }
    final String nf;
    if (!conf.hasPath("application.numberFormat")) {
      nf = Optional.ofNullable(numberFormat)
          .orElseGet(() -> ((DecimalFormat) DecimalFormat.getInstance(locale)).toPattern());
    } else {
      nf = conf.getString("application.numberFormat");
    }
    int processors = Runtime.getRuntime().availableProcessors();
    String version = Optional.ofNullable(getClass().getPackage())
        .map(Package::getImplementationVersion)
        .filter(Objects::nonNull)
        .orElse("0.0.0");
    Config defs = ConfigFactory.parseResources(Jooby.class, "jooby.conf")
        .withValue("contextPath", fromAnyRef(cpath.equals("/") ? "" : cpath))
        .withValue("application.name", fromAnyRef(appname))
        .withValue("application.version", fromAnyRef(version))
        .withValue("application.class", fromAnyRef(classname))
        .withValue("application.ns", fromAnyRef(ns))
        .withValue("application.lang", fromAnyRef(lang))
        .withValue("application.tz", fromAnyRef(tz))
        .withValue("application.numberFormat", fromAnyRef(nf))
        .withValue("server.http2.enabled", fromAnyRef(http2))
        .withValue("runtime.processors", fromAnyRef(processors))
        .withValue("runtime.processors-plus1", fromAnyRef(processors + 1))
        .withValue("runtime.processors-plus2", fromAnyRef(processors + 2))
        .withValue("runtime.processors-x2", fromAnyRef(processors * 2))
        .withValue("runtime.processors-x4", fromAnyRef(processors * 4))
        .withValue("runtime.processors-x8", fromAnyRef(processors * 8))
        .withValue("runtime.concurrencyLevel", fromAnyRef(Math.max(4, processors)))
        .withValue("server.threads.Min", fromAnyRef(Math.max(4, processors)))
        .withValue("server.threads.Max", fromAnyRef(Math.max(32, processors * 8)));
    if (charset != null) {
      defs = defs.withValue("application.charset", fromAnyRef(charset.name()));
    }
    if (port != null) {
      defs = defs.withValue("application.port", fromAnyRef(port));
    }
    if (securePort != null) {
      defs = defs.withValue("application.securePort", fromAnyRef(securePort));
    }
    if (dateFormat != null) {
      defs = defs.withValue("application.dateFormat", fromAnyRef(dateFormat));
    }
    return defs;
  }
  private static void install(final Logger log, final Jooby.Module module, final Env env, final Config config,
      final Binder binder) throws Throwable {
    module.configure(env, config, binder);
    try {
      binder.install(ProviderMethodsModule.forObject(module));
    } catch (NoClassDefFoundError x) {
      log.debug("ignoring class not found from guice provider method", x);
    }
  }
  @SuppressWarnings("unchecked")
  private void bindConfig(final Binder binder, final Config config) {
    traverse(binder, "", config.root());
    for (Entry<String, ConfigValue> entry : config.entrySet()) {
      String name = entry.getKey();
      Named named = Names.named(name);
      Object value = entry.getValue().unwrapped();
      if (value instanceof List) {
        List<Object> values = (List<Object>) value;
        Type listType = values.size() == 0
            ? String.class
            : Types.listOf(values.iterator().next().getClass());
        Key<Object> key = (Key<Object>) Key.get(listType, Names.named(name));
        binder.bind(key).toInstance(values);
      } else {
        binder.bindConstant().annotatedWith(named).to(value.toString());
      }
    }
    binder.bind(Config.class).toInstance(config);
  }
  private static void traverse(final Binder binder, final String p, final ConfigObject root) {
    root.forEach((n, v) -> {
      if (v instanceof ConfigObject) {
        ConfigObject child = (ConfigObject) v;
        String path = p + n;
        Named named = Names.named(path);
        binder.bind(Config.class).annotatedWith(named).toInstance(child.toConfig());
        traverse(binder, path + ".", child);
      }
    });
  }
  private static Predicate<String> envpredicate(final String candidate) {
    return env -> env.equalsIgnoreCase(candidate) || candidate.equals("*");
  }
  static String logback(final Config conf) {
    String logback;
    if (conf.hasPath("logback.configurationFile")) {
      logback = conf.getString("logback.configurationFile");
    } else {
      String env = conf.hasPath("application.env") ? conf.getString("application.env") : null;
      ImmutableList.Builder<File> files = ImmutableList.builder();
      File userdir = new File(System.getProperty("user.dir"));
      File confdir = new File(userdir, "conf");
      if (env != null) {
        files.add(new File(userdir, "logback." + env + ".xml"));
        files.add(new File(confdir, "logback." + env + ".xml"));
      }
      files.add(new File(userdir, "logback.xml"));
      files.add(new File(confdir, "logback.xml"));
      logback = files.build()
          .stream()
          .filter(File::exists)
          .map(File::getAbsolutePath)
          .findFirst()
          .orElseGet(() -> {
            return Optional.ofNullable(Jooby.class.getResource("/logback." + env + ".xml"))
                .map(Objects::toString)
                .orElse("logback.xml");
          });
    }
    return logback;
  }
  private static Logger logger(final Jooby app) {
    return LoggerFactory.getLogger(app.getClass());
  }
  private Route.AssetDefinition configureAssetHandler(final Route.AssetDefinition handler) {
    onStart(r -> {
      Config conf = r.require(Config.class);
      handler
          .cdn(conf.getString("assets.cdn"))
          .lastModified(conf.getBoolean("assets.lastModified"))
          .etag(conf.getBoolean("assets.etag"))
          .maxAge(conf.getString("assets.cache.maxAge"));
    });
    return handler;
  }
  private String classname(String name) {
    if (name.equals(Jooby.class.getName()) || name.equals("org.jooby.Kooby")) {
      return SourceProvider.INSTANCE.get()
          .map(StackTraceElement::getClassName)
          .orElse(name);
    }
    return name;
  }
}
