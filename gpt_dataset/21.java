import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

@WebServlet("/InsecureQueryStringServlet")
public class InsecureQueryStringServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String queryString = request.getQueryString(); // Get the query string from the request

        response.setContentType("text/html");
        response.getWriter().println("Query string: " + queryString); // Vulnerable to CWE-15
    }
}

