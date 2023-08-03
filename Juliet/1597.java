
package testcases.CWE113_HTTP_Response_Splitting.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.net.URLEncoder;
public class CWE113_HTTP_Response_Splitting__File_addHeaderServlet_67b
{
    public void badSink(CWE113_HTTP_Response_Splitting__File_addHeaderServlet_67a.Container dataContainer , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataContainer.containerOne;
        if (data != null)
        {
            response.addHeader("Location", "/author.jsp?lang=" + data);
        }
    }
    public void goodG2BSink(CWE113_HTTP_Response_Splitting__File_addHeaderServlet_67a.Container dataContainer , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataContainer.containerOne;
        if (data != null)
        {
            response.addHeader("Location", "/author.jsp?lang=" + data);
        }
    }
    public void goodB2GSink(CWE113_HTTP_Response_Splitting__File_addHeaderServlet_67a.Container dataContainer , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataContainer.containerOne;
        if (data != null)
        {
            data = URLEncoder.encode(data, "UTF-8");
            response.addHeader("Location", "/author.jsp?lang=" + data);
        }
    }
}
