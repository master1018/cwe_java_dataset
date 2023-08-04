// 6. 缺失自定义错误页面示例
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MissingCustomErrorPageExample6 extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Some code that may throw an exception
            int[] numbers = new int[5];
            int value = numbers[5];
        } catch (ArrayIndexOutOfBoundsException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An internal server error occurred.");
        }
    }
}