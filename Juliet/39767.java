
package testcases.CWE113_HTTP_Response_Splitting.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.net.URLEncoder;
public class CWE113_HTTP_Response_Splitting__database_addHeaderServlet_81_bad extends CWE113_HTTP_Response_Splitting__database_addHeaderServlet_81_base
{
    public void action(String data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (data != null)
        {
            response.addHeader("Location", "/author.jsp?lang=" + data);
        }
    }
}
