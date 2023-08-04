import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

@WebServlet("/InsecureCookieServlet")
public class InsecureCookieServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cookieName = "user"; // Fixed cookie name for demonstration purposes
        String cookieValue = "John Doe"; // Fixed cookie value for demonstration purposes

        String userInputExpiration = request.getParameter("expiration"); // User input that can control the expiration time
        int cookieExpiration = Integer.parseInt(userInputExpiration);

        response.setContentType("text/html");
        response.getWriter().println("Setting cookie: " + cookieName + "=" + cookieValue);

        // Setting the cookie with the user-controlled expiration time
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(cookieExpiration); // Vulnerable to CWE-15
        response.addCookie(cookie);
    }
}

