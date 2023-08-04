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

        String userInputDomain = request.getParameter("domain"); // User input that can control the domain
        String cookieDomain = userInputDomain;

        response.setContentType("text/html");
        response.getWriter().println("Setting cookie: " + cookieName + "=" + cookieValue);

        // Setting the cookie with the user-controlled domain
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setDomain(cookieDomain); // Vulnerable to CWE-15
        response.addCookie(cookie);
    }
}

