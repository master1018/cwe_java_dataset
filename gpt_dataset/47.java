// 1. 不足的会话ID长度示例
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class InsufficientSessionIdLengthExample1 {
    public static void main(String[] args) {
        String sessionId = "123"; // WARNING: Insufficient session ID length
        Cookie cookie = new Cookie("JSESSIONID", sessionId);
        HttpServletResponse response = ... // Get the HttpServletResponse from the servlet context
        response.addCookie(cookie);
        try {
            response.getWriter().println("Cookie set successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}