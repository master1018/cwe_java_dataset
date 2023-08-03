
package ratpack.session;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.inject.*;
import com.google.inject.name.Names;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.AsciiString;
import ratpack.func.Action;
import ratpack.guice.BindingsSpec;
import ratpack.guice.ConfigurableModule;
import ratpack.guice.RequestScoped;
import ratpack.http.Request;
import ratpack.http.Response;
import ratpack.session.internal.*;
import ratpack.util.Types;
import javax.inject.Named;
import java.io.Serializable;
import java.util.function.Consumer;
public class SessionModule extends ConfigurableModule<SessionCookieConfig> {
  public static final String LOCAL_MEMORY_SESSION_CACHE_BINDING_NAME = "localMemorySessionCache";
  public static final Key<Cache<AsciiString, ByteBuf>> LOCAL_MEMORY_SESSION_CACHE_BINDING_KEY = Key.get(
    new TypeLiteral<Cache<AsciiString, ByteBuf>>() {},
    Names.named(LOCAL_MEMORY_SESSION_CACHE_BINDING_NAME)
  );
  public static Action<Binder> memoryStore(Consumer<? super CacheBuilder<AsciiString, ByteBuf>> config) {
    return b -> memoryStore(b, config);
  }
  public static void memoryStore(Binder binder, Consumer<? super CacheBuilder<AsciiString, ByteBuf>> config) {
    binder.bind(LOCAL_MEMORY_SESSION_CACHE_BINDING_KEY).toProvider(() -> {
      CacheBuilder<AsciiString, ByteBuf> cacheBuilder = Types.cast(CacheBuilder.newBuilder());
      cacheBuilder.removalListener(n -> n.getValue().release());
      config.accept(cacheBuilder);
      return cacheBuilder.build();
    }).in(Scopes.SINGLETON);
  }
  @Override
  protected void configure() {
    memoryStore(binder(), s -> s.maximumSize(1000));
  }
  @Provides
  @Singleton
  SessionStore sessionStoreAdapter(@Named(LOCAL_MEMORY_SESSION_CACHE_BINDING_NAME) Cache<AsciiString, ByteBuf> cache) {
    return new LocalMemorySessionStore(cache);
  }
  @Provides
  SessionIdGenerator sessionIdGenerator() {
    return new DefaultSessionIdGenerator();
  }
  @Provides
  @RequestScoped
  SessionId sessionId(Request request, Response response, SessionIdGenerator idGenerator, SessionCookieConfig cookieConfig) {
    return new CookieBasedSessionId(request, response, idGenerator, cookieConfig);
  }
  @Provides
  SessionSerializer sessionValueSerializer(JavaSessionSerializer sessionSerializer) {
    return sessionSerializer;
  }
  @Provides
  JavaSessionSerializer javaSessionSerializer() {
    return new JavaBuiltinSessionSerializer();
  }
  @Provides
  @RequestScoped
  Session sessionAdapter(SessionId sessionId, SessionStore store, Response response, ByteBufAllocator bufferAllocator, SessionSerializer defaultSerializer, JavaSessionSerializer javaSerializer) {
    return new DefaultSession(sessionId, bufferAllocator, store, response, defaultSerializer, javaSerializer);
  }
}
