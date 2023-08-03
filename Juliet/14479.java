
package testcases.CWE113_HTTP_Response_Splitting.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.net.URLEncoder;
public class CWE113_HTTP_Response_Splitting__getCookies_Servlet_addHeaderServlet_61a extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = (new CWE113_HTTP_Response_Splitting__getCookies_Servlet_addHeaderServlet_61b()).badSource(request, response);
        if (data != null)
        {
            response.addHeader("Location", "/author.jsp?lang=" + data);
        }
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = (new CWE113_HTTP_Response_Splitting__getCookies_Servlet_addHeaderServlet_61b()).goodG2BSource(request, response);
        if (data != null)
        {
            response.addHeader("Location", "/author.jsp?lang=" + data);
        }
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = (new CWE113_HTTP_Response_Splitting__getCookies_Servlet_addHeaderServlet_61b()).goodB2GSource(request, response);
        if (data != null)
        {
            data = URLEncoder.encode(data, "UTF-8");
            response.addHeader("Location", "/author.jsp?lang=" + data);
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
