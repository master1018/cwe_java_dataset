
package ratpack.session.internal;
import com.google.inject.Singleton;
import io.netty.util.AsciiString;
import ratpack.session.SessionIdGenerator;
import java.util.UUID;
@Singleton
public class DefaultSessionIdGenerator implements SessionIdGenerator {
  public AsciiString generateSessionId() {
    return AsciiString.cached(UUID.randomUUID().toString());
  }
}
