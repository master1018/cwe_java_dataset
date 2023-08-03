
package testcases.CWE113_HTTP_Response_Splitting.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.net.URLEncoder;
public class CWE113_HTTP_Response_Splitting__connect_tcp_addHeaderServlet_22b
{
    public void badSink(String data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (CWE113_HTTP_Response_Splitting__connect_tcp_addHeaderServlet_22a.badPublicStatic)
        {
            if (data != null)
            {
                response.addHeader("Location", "/author.jsp?lang=" + data);
            }
        }
        else
        {
            data = null;
        }
    }
    public void goodB2G1Sink(String data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (CWE113_HTTP_Response_Splitting__connect_tcp_addHeaderServlet_22a.goodB2G1PublicStatic)
        {
            data = null;
        }
        else
        {
            if (data != null)
            {
                data = URLEncoder.encode(data, "UTF-8");
                response.addHeader("Location", "/author.jsp?lang=" + data);
            }
        }
    }
    public void goodB2G2Sink(String data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (CWE113_HTTP_Response_Splitting__connect_tcp_addHeaderServlet_22a.goodB2G2PublicStatic)
        {
            if (data != null)
            {
                data = URLEncoder.encode(data, "UTF-8");
                response.addHeader("Location", "/author.jsp?lang=" + data);
            }
        }
        else
        {
            data = null;
        }
    }
    public void goodG2BSink(String data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (CWE113_HTTP_Response_Splitting__connect_tcp_addHeaderServlet_22a.goodG2BPublicStatic)
        {
            if (data != null)
            {
                response.addHeader("Location", "/author.jsp?lang=" + data);
            }
        }
        else
        {
            data = null;
        }
    }
}
