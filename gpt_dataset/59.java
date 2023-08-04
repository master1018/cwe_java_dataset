// 3. 缺失自定义错误页面示例
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MissingCustomErrorPageExample3 extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Some code that may throw an exception
            String[] items = {"item1", "item2"};
            String item = items[2];
        } catch (ArrayIndexOutOfBoundsException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Item not found.");
        }
    }
}