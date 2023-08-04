//6. 不安全的会话管理：

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "InsecureSessionServlet", urlPatterns = {"/insecure-session"})
public class InsecureSessionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        // WARNING: Insecure session management without secure attributes
        request.getSession().setAttribute("username", "exampleUser");
        try {
            response.getWriter().println("Session set successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}