import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

@WebServlet("/InsecureDatabaseServlet")
public class InsecureDatabaseServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String queryString = request.getQueryString(); // Get the query string from the request

        // Perform database query with the user-controlled query string
        // Note: In a real-world scenario, use parameterized queries to prevent SQL injection.

        response.setContentType("text/html");
        response.getWriter().println("Executing query with query string: " + queryString); // Vulnerable to CWE-15
    }
}

