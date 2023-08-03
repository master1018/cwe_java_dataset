
package testcases.CWE83_XSS_Attribute;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE83_XSS_Attribute__Servlet_listen_tcp_66b
{
    public void badSink(String dataArray[] , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataArray[2];
        if (data != null)
        {
            response.getWriter().println("<br>bad() - <img src=\"" + data + "\">");
        }
    }
    public void goodG2BSink(String dataArray[] , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataArray[2];
        if (data != null)
        {
            response.getWriter().println("<br>bad() - <img src=\"" + data + "\">");
        }
    }
}
