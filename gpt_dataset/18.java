import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

@WebServlet("/InsecureParameterServlet")
public class InsecureParameterServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userInput = request.getParameter("userInput"); // User input that can control the output

        response.setContentType("text/html");
        response.getWriter().println("User input: " + userInput); // Vulnerable to CWE-15
    }
}

