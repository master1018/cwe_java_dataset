import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

@WebServlet("/InsecureDatabaseServlet")
public class InsecureDatabaseServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username"); // User input that can control the database query

        // Perform database query with the user-controlled username
        // Note: In a real-world scenario, use parameterized queries to prevent SQL injection.

        response.setContentType("text/html");
        response.getWriter().println("Executing query for username: " + username); // Vulnerable to CWE-15
    }
}

