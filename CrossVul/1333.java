
package org.jooby;
import com.google.common.base.CaseFormat;
import static com.google.common.base.Preconditions.checkArgument;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.primitives.Primitives;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import static java.util.Objects.requireNonNull;
import org.jooby.funzy.Throwing;
import org.jooby.handlers.AssetHandler;
import org.jooby.internal.RouteImpl;
import org.jooby.internal.RouteMatcher;
import org.jooby.internal.RoutePattern;
import org.jooby.internal.RouteSourceImpl;
import org.jooby.internal.SourceProvider;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
public interface Route {
  interface Source {
    Source BUILTIN = new Source() {
      @Override
      public int line() {
        return -1;
      }
      @Override
      public Optional<String> declaringClass() {
        return Optional.empty();
      }
      @Override
      public String toString() {
        return "~builtin";
      }
    };
    int line();
    @Nonnull
    Optional<String> declaringClass();
  }
  interface Mapper<T> {
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Nonnull
    static Mapper<Object> chain(final Mapper it, final Mapper next) {
      return create(it.name() + ">" + next.name(), v -> next.map(it.map(v)));
    }
    @Nonnull
    static <T> Mapper<T> create(final String name, final Throwing.Function<T, Object> fn) {
      return new Route.Mapper<T>() {
        @Override
        public String name() {
          return name;
        }
        @Override
        public Object map(final T value) throws Throwable {
          return fn.apply(value);
        }
        @Override
        public String toString() {
          return name();
        }
      };
    }
    @Nonnull
    default String name() {
      String name = Optional.ofNullable(Strings.emptyToNull(getClass().getSimpleName()))
          .orElseGet(() -> {
            String classname = getClass().getName();
            return classname.substring(classname.lastIndexOf('.') + 1);
          });
      return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, name);
    }
    @Nonnull
    Object map(T value) throws Throwable;
  }
  interface Props<T extends Props<T>> {
    @Nonnull
    T attr(String name, Object value);
    @Nonnull
    T renderer(final String name);
    @Nullable
    String renderer();
    @Nonnull
    T name(final String name);
    @Nonnull
    default T consumes(final MediaType... consumes) {
      return consumes(Arrays.asList(consumes));
    }
    @Nonnull
    default T consumes(final String... consumes) {
      return consumes(MediaType.valueOf(consumes));
    }
    @Nonnull
    T consumes(final List<MediaType> consumes);
    @Nonnull
    default T produces(final MediaType... produces) {
      return produces(Arrays.asList(produces));
    }
    @Nonnull
    default T produces(final String... produces) {
      return produces(MediaType.valueOf(produces));
    }
    @Nonnull
    T produces(final List<MediaType> produces);
    @Nonnull
    default T excludes(final String... excludes) {
      return excludes(Arrays.asList(excludes));
    }
    @Nonnull
    T excludes(final List<String> excludes);
    @Nonnull
    T map(Mapper<?> mapper);
  }
  @SuppressWarnings({"unchecked", "rawtypes"}) class Collection implements Props<Collection> {
    private final Route.Props[] routes;
    public Collection(final Route.Props... definitions) {
      this.routes = requireNonNull(definitions, "Route definitions are required.");
    }
    @Override
    public Collection name(final String name) {
      for (Props definition : routes) {
        definition.name(name);
      }
      return this;
    }
    @Override
    public String renderer() {
      return routes[0].renderer();
    }
    @Override
    public Collection renderer(final String name) {
      for (Props definition : routes) {
        definition.renderer(name);
      }
      return this;
    }
    @Override
    public Collection consumes(final List<MediaType> types) {
      for (Props definition : routes) {
        definition.consumes(types);
      }
      return this;
    }
    @Override
    public Collection produces(final List<MediaType> types) {
      for (Props definition : routes) {
        definition.produces(types);
      }
      return this;
    }
    @Override
    public Collection attr(final String name, final Object value) {
      for (Props definition : routes) {
        definition.attr(name, value);
      }
      return this;
    }
    @Override
    public Collection excludes(final List<String> excludes) {
      for (Props definition : routes) {
        definition.excludes(excludes);
      }
      return this;
    }
    @Override
    public Collection map(final Mapper<?> mapper) {
      for (Props route : routes) {
        route.map(mapper);
      }
      return this;
    }
  }
  class Definition implements Props<Definition> {
    private String name = "/anonymous";
    private RoutePattern cpattern;
    private Filter filter;
    private List<MediaType> consumes = MediaType.ALL;
    private List<MediaType> produces = MediaType.ALL;
    private String method;
    private String pattern;
    private List<RoutePattern> excludes = Collections.emptyList();
    private Map<String, Object> attributes = ImmutableMap.of();
    private Mapper<?> mapper;
    private int line;
    private String declaringClass;
    String prefix;
    private String renderer;
    public Definition(final String verb, final String pattern,
        final Route.Handler handler) {
      this(verb, pattern, (Route.Filter) handler);
    }
    public Definition(final String verb, final String pattern,
        final Route.Handler handler, boolean caseSensitiveRouting) {
      this(verb, pattern, (Route.Filter) handler, caseSensitiveRouting);
    }
    public Definition(final String verb, final String pattern,
        final Route.OneArgHandler handler) {
      this(verb, pattern, (Route.Filter) handler);
    }
    public Definition(final String verb, final String pattern,
        final Route.ZeroArgHandler handler) {
      this(verb, pattern, (Route.Filter) handler);
    }
    public Definition(final String method, final String pattern, final Filter filter) {
      this(method, pattern, filter, true);
    }
    public Definition(final String method, final String pattern,
        final Filter filter, boolean caseSensitiveRouting) {
      requireNonNull(pattern, "A route path is required.");
      requireNonNull(filter, "A filter is required.");
      this.method = method.toUpperCase();
      this.cpattern = new RoutePattern(method, pattern, !caseSensitiveRouting);
      this.pattern = cpattern.pattern();
      this.filter = filter;
      SourceProvider.INSTANCE.get().ifPresent(source -> {
        this.line = source.getLineNumber();
        this.declaringClass = source.getClassName();
      });
    }
    @Nonnull
    public String pattern() {
      return pattern;
    }
    @Nullable
    public String renderer() {
      return renderer;
    }
    @Override
    public Definition renderer(final String name) {
      this.renderer = name;
      return this;
    }
    @Nonnull
    public List<String> vars() {
      return cpattern.vars();
    }
    @Nonnull
    public boolean glob() {
      return cpattern.glob();
    }
    @Nonnull
    public Route.Source source() {
      return new RouteSourceImpl(declaringClass, line);
    }
    @Nonnull
    public String reverse(final Map<String, Object> vars) {
      return cpattern.reverse(vars);
    }
    @Nonnull
    public String reverse(final Object... values) {
      return cpattern.reverse(values);
    }
    @Override
    @Nonnull
    public Definition attr(final String name, final Object value) {
      requireNonNull(name, "Attribute name is required.");
      requireNonNull(value, "Attribute value is required.");
      if (valid(value)) {
        attributes = ImmutableMap.<String, Object>builder()
            .putAll(attributes)
            .put(name, value)
            .build();
      }
      return this;
    }
    private boolean valid(final Object value) {
      if (Primitives.isWrapperType(Primitives.wrap(value.getClass()))) {
        return true;
      }
      if (value instanceof String || value instanceof Enum || value instanceof Class) {
        return true;
      }
      if (value.getClass().isArray() && Array.getLength(value) > 0) {
        return valid(Array.get(value, 0));
      }
      if (value instanceof Map && ((Map) value).size() > 0) {
        Map.Entry e = (Map.Entry) ((Map) value).entrySet().iterator().next();
        return valid(e.getKey()) && valid(e.getValue());
      }
      return false;
    }
    @SuppressWarnings("unchecked")
    @Nonnull
    public <T> T attr(final String name) {
      return (T) attributes.get(name);
    }
    @Nonnull
    public Map<String, Object> attributes() {
      return attributes;
    }
    @Nonnull
    public Optional<Route> matches(final String method,
        final String path, final MediaType contentType,
        final List<MediaType> accept) {
      String fpath = method + path;
      if (excludes.size() > 0 && excludes(fpath)) {
        return Optional.empty();
      }
      RouteMatcher matcher = cpattern.matcher(fpath);
      if (matcher.matches()) {
        List<MediaType> result = MediaType.matcher(accept).filter(this.produces);
        if (result.size() > 0 && canConsume(contentType)) {
          List<MediaType> produces = result.size() == 1 && result.get(0).name().equals("*
    @Nonnull
    public String method() {
      return method;
    }
    @Nonnull
    public Route.Filter filter() {
      return filter;
    }
    @Nonnull
    public String name() {
      return name;
    }
    @Override
    @Nonnull
    public Definition name(final String name) {
      checkArgument(!Strings.isNullOrEmpty(name), "A route's name is required.");
      this.name = normalize(prefix != null ? prefix + "/" + name : name);
      return this;
    }
    public boolean canConsume(final MediaType type) {
      return MediaType.matcher(Arrays.asList(type)).matches(consumes);
    }
    public boolean canConsume(final String type) {
      return MediaType.matcher(MediaType.valueOf(type)).matches(consumes);
    }
    public boolean canProduce(final List<MediaType> types) {
      return MediaType.matcher(types).matches(produces);
    }
    public boolean canProduce(final MediaType... types) {
      return canProduce(Arrays.asList(types));
    }
    public boolean canProduce(final String... types) {
      return canProduce(MediaType.valueOf(types));
    }
    @Override
    public Definition consumes(final List<MediaType> types) {
      checkArgument(types != null && types.size() > 0, "Consumes types are required");
      if (types.size() > 1) {
        this.consumes = Lists.newLinkedList(types);
        Collections.sort(this.consumes);
      } else {
        this.consumes = ImmutableList.of(types.get(0));
      }
      return this;
    }
    @Override
    public Definition produces(final List<MediaType> types) {
      checkArgument(types != null && types.size() > 0, "Produces types are required");
      if (types.size() > 1) {
        this.produces = Lists.newLinkedList(types);
        Collections.sort(this.produces);
      } else {
        this.produces = ImmutableList.of(types.get(0));
      }
      return this;
    }
    @Override
    public Definition excludes(final List<String> excludes) {
      this.excludes = excludes.stream()
          .map(it -> new RoutePattern(method, it))
          .collect(Collectors.toList());
      return this;
    }
    @Nonnull
    public List<String> excludes() {
      return excludes.stream().map(r -> r.pattern()).collect(Collectors.toList());
    }
    private boolean excludes(final String path) {
      for (RoutePattern pattern : excludes) {
        if (pattern.matcher(path).matches()) {
          return true;
        }
      }
      return false;
    }
    @Nonnull
    public List<MediaType> consumes() {
      return Collections.unmodifiableList(this.consumes);
    }
    @Nonnull
    public List<MediaType> produces() {
      return Collections.unmodifiableList(this.produces);
    }
    @Override
    @Nonnull
    public Definition map(final Mapper<?> mapper) {
      this.mapper = requireNonNull(mapper, "Mapper is required.");
      return this;
    }
    @Nonnull
    public Definition line(final int line) {
      this.line = line;
      return this;
    }
    @Nonnull
    public Definition declaringClass(final String declaringClass) {
      this.declaringClass = declaringClass;
      return this;
    }
    @Override
    public String toString() {
      StringBuilder buffer = new StringBuilder();
      buffer.append(method()).append(" ").append(pattern()).append("\n");
      buffer.append("  name: ").append(name()).append("\n");
      buffer.append("  excludes: ").append(excludes).append("\n");
      buffer.append("  consumes: ").append(consumes()).append("\n");
      buffer.append("  produces: ").append(produces()).append("\n");
      return buffer.toString();
    }
    private Route asRoute(final String method, final RouteMatcher matcher,
        final List<MediaType> produces, final Route.Source source) {
      return new RouteImpl(filter, this, method, matcher.path(), produces,
          matcher.vars(), mapper, source);
    }
  }
  class Forwarding implements Route {
    private final Route route;
    public Forwarding(final Route route) {
      this.route = route;
    }
    @Override
    public String renderer() {
      return route.renderer();
    }
    @Override
    public String path() {
      return route.path();
    }
    @Override
    public String method() {
      return route.method();
    }
    @Override
    public String pattern() {
      return route.pattern();
    }
    @Override
    public String name() {
      return route.name();
    }
    @Override
    public Map<Object, String> vars() {
      return route.vars();
    }
    @Override
    public List<MediaType> consumes() {
      return route.consumes();
    }
    @Override
    public List<MediaType> produces() {
      return route.produces();
    }
    @Override
    public Map<String, Object> attributes() {
      return route.attributes();
    }
    @Override
    public <T> T attr(final String name) {
      return route.attr(name);
    }
    @Override
    public boolean glob() {
      return route.glob();
    }
    @Override
    public String reverse(final Map<String, Object> vars) {
      return route.reverse(vars);
    }
    @Override
    public String reverse(final Object... values) {
      return route.reverse(values);
    }
    @Override
    public Source source() {
      return route.source();
    }
    @Override
    public String print() {
      return route.print();
    }
    @Override
    public String print(final int indent) {
      return route.print(indent);
    }
    @Override
    public String toString() {
      return route.toString();
    }
    public static Route unwrap(final Route route) {
      Route root = route;
      while (root instanceof Forwarding) {
        root = ((Forwarding) root).route;
      }
      return root;
    }
  }
  public interface Filter {
    void handle(Request req, Response rsp, Route.Chain chain) throws Throwable;
  }
  class AssetDefinition extends Definition {
    private Boolean etag;
    private String cdn;
    private Object maxAge;
    private Boolean lastModifiedSince;
    private Integer statusCode;
    public AssetDefinition(final String method, final String pattern,
        final Route.Filter handler, boolean caseSensitiveRouting) {
      super(method, pattern, handler, caseSensitiveRouting);
    }
    @Nonnull
    @Override
    public AssetHandler filter() {
      return (AssetHandler) super.filter();
    }
    public AssetDefinition onMissing(final int statusCode) {
      if (this.statusCode == null) {
        filter().onMissing(statusCode);
        this.statusCode = statusCode;
      }
      return this;
    }
    public AssetDefinition etag(final boolean etag) {
      if (this.etag == null) {
        filter().etag(etag);
        this.etag = etag;
      }
      return this;
    }
    public AssetDefinition lastModified(final boolean enabled) {
      if (this.lastModifiedSince == null) {
        filter().lastModified(enabled);
        this.lastModifiedSince = enabled;
      }
      return this;
    }
    public AssetDefinition cdn(final String cdn) {
      if (this.cdn == null) {
        filter().cdn(cdn);
        this.cdn = cdn;
      }
      return this;
    }
    public AssetDefinition maxAge(final Duration maxAge) {
      if (this.maxAge == null) {
        filter().maxAge(maxAge);
        this.maxAge = maxAge;
      }
      return this;
    }
    public AssetDefinition maxAge(final long maxAge) {
      if (this.maxAge == null) {
        filter().maxAge(maxAge);
        this.maxAge = maxAge;
      }
      return this;
    }
    public AssetDefinition maxAge(final String maxAge) {
      if (this.maxAge == null) {
        filter().maxAge(maxAge);
        this.maxAge = maxAge;
      }
      return this;
    }
  }
  interface Handler extends Filter {
    @Override
    default void handle(final Request req, final Response rsp, final Route.Chain chain)
        throws Throwable {
      handle(req, rsp);
      chain.next(req, rsp);
    }
    void handle(Request req, Response rsp) throws Throwable;
  }
  interface MethodHandler extends Handler {
    @Nonnull
    Method method();
    @Nonnull
    Class<?> implementingClass();
  }
  interface OneArgHandler extends Filter {
    @Override
    default void handle(final Request req, final Response rsp, final Route.Chain chain)
        throws Throwable {
      Object result = handle(req);
      rsp.send(result);
      chain.next(req, rsp);
    }
    Object handle(Request req) throws Throwable;
  }
  interface ZeroArgHandler extends Filter {
    @Override
    default void handle(final Request req, final Response rsp, final Route.Chain chain)
        throws Throwable {
      Object result = handle();
      rsp.send(result);
      chain.next(req, rsp);
    }
    Object handle() throws Throwable;
  }
  interface Before extends Route.Filter {
    @Override
    default void handle(final Request req, final Response rsp, final Chain chain) throws Throwable {
      handle(req, rsp);
      chain.next(req, rsp);
    }
    void handle(Request req, Response rsp) throws Throwable;
  }
  interface After extends Filter {
    @Override
    default void handle(final Request req, final Response rsp, final Chain chain) throws Throwable {
      rsp.after(this);
      chain.next(req, rsp);
    }
    Result handle(Request req, Response rsp, Result result) throws Exception;
  }
  interface Complete extends Filter {
    @Override
    default void handle(final Request req, final Response rsp, final Chain chain) throws Throwable {
      rsp.complete(this);
      chain.next(req, rsp);
    }
    void handle(Request req, Response rsp, Optional<Throwable> cause);
  }
  interface Chain {
    void next(@Nullable String prefix, Request req, Response rsp) throws Throwable;
    default void next(final Request req, final Response rsp) throws Throwable {
      next(null, req, rsp);
    }
    List<Route> routes();
  }
  Key<Set<Route.Definition>> KEY = Key.get(new TypeLiteral<Set<Route.Definition>>() {
  });
  char OUT_OF_PATH = '\u200B';
  String GET = "GET";
  String POST = "POST";
  String PUT = "PUT";
  String DELETE = "DELETE";
  String PATCH = "PATCH";
  String HEAD = "HEAD";
  String CONNECT = "CONNECT";
  String OPTIONS = "OPTIONS";
  String TRACE = "TRACE";
  List<String> METHODS = ImmutableList.<String>builder()
      .add(GET,
          POST,
          PUT,
          DELETE,
          PATCH,
          HEAD,
          CONNECT,
          OPTIONS,
          TRACE)
      .build();
  @Nonnull
  String path();
  @Nonnull
  String method();
  @Nonnull
  String pattern();
  @Nonnull
  String name();
  @Nonnull
  Map<Object, String> vars();
  @Nonnull
  List<MediaType> consumes();
  @Nonnull
  List<MediaType> produces();
  default boolean apply(final String prefix) {
    return name().startsWith(prefix);
  }
  @Nonnull
  Map<String, Object> attributes();
  @SuppressWarnings("unchecked")
  @Nonnull
  default <T> T attr(final String name) {
    return (T) attributes().get(name);
  }
  @Nonnull
  String renderer();
  boolean glob();
  @Nonnull
  String reverse(final Map<String, Object> vars);
  @Nonnull
  String reverse(final Object... values);
  @Nonnull
  static String normalize(final String path) {
    return RoutePattern.normalize(path);
  }
  @Nonnull
  static String unerrpath(final String path) {
    if (path.charAt(0) == OUT_OF_PATH) {
      return path.substring(1);
    }
    return path;
  }
  @Nonnull
  static String errpath(final String path) {
    return OUT_OF_PATH + path;
  }
  @Nonnull
  Route.Source source();
  @Nonnull
  default String print(final int indent) {
    StringBuilder buff = new StringBuilder();
    String[] header = {"Method", "Path", "Source", "Name", "Pattern", "Consumes", "Produces"};
    String[] values = {method(), path(), source().toString(), name(), pattern(),
        consumes().toString(), produces().toString()};
    BiConsumer<Function<Integer, String>, Character> format = (v, s) -> {
      buff.append(Strings.padEnd("", indent, ' '))
          .append("|").append(s);
      for (int i = 0; i < header.length; i++) {
        buff
            .append(Strings.padEnd(v.apply(i), Math.max(header[i].length(), values[i].length()), s))
            .append(s).append("|").append(s);
      }
      buff.setLength(buff.length() - 1);
    };
    format.accept(i -> header[i], ' ');
    buff.append("\n");
    format.accept(i -> "-", '-');
    buff.append("\n");
    format.accept(i -> values[i], ' ');
    return buff.toString();
  }
  @Nonnull
  default String print() {
    return print(0);
  }
}
