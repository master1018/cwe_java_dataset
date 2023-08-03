
package ratpack.session.internal;
import io.netty.util.AsciiString;
import ratpack.session.SessionIdGenerator;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
public class DefaultSessionIdGenerator implements SessionIdGenerator {
  public AsciiString generateSessionId() {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    UUID uuid = new UUID(random.nextLong(), random.nextLong());
    return AsciiString.of(uuid.toString());
  }
}
