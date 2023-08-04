import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

@WebServlet("/InsecureCookieServlet")
public class InsecureCookieServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cookieName = request.getParameter("name"); // User input that can control the cookie name
        String cookieValue = request.getParameter("value"); // User input that can control the cookie value

        response.setContentType("text/html");
        response.getWriter().println("Setting cookie: " + cookieName + "=" + cookieValue);

        // Setting the cookie to the response
        response.addCookie(new Cookie(cookieName, cookieValue)); // Vulnerable to CWE-15
    }
}

