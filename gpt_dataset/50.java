// 4. 不足的会话ID长度示例
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;

public class InsufficientSessionIdLengthExample4 {
    public static void main(String[] args) {
        HttpServletRequest request = ... // Get the HttpServletRequest from the servlet context
        String sessionId = UUID.randomUUID().toString().substring(0, 10); // WARNING: Insufficient session ID length
        // ...
    }
}