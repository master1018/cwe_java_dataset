// 8. 缺失自定义错误页面示例
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MissingCustomErrorPageExample8 extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Some code that may throw an exception
            String resource = "config.properties";
            ClassLoader classLoader = getClass().getClassLoader();
            classLoader.getResourceAsStream(resource);
        } catch (NullPointerException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource not found.");
        }
    }
}