//3. 不安全的Cookie传输：

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class InsecureCookieExample {
    public static void main(String[] args) {
        String username = "exampleUser";

        // WARNING: Insecure cookie transmission without encryption
        Cookie cookie = new Cookie("username", username);
        HttpServletResponse response = ... // Get the HttpServletResponse from the servlet context
        response.addCookie(cookie);
        try {
            response.getWriter().println("Cookie set successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}