// 10. 不足的会话ID长度示例
import java.security.SecureRandom;
import javax.servlet.http.HttpServletRequest;

public class InsufficientSessionIdLengthExample10 {
    public static void main(String[] args) {
        HttpServletRequest request = ... // Get the HttpServletRequest from the servlet context
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[4];
        random.nextBytes(bytes);
        String sessionId = new String(bytes); // WARNING: Insufficient session ID length
        // ...
    }
}