
package testcases.CWE80_XSS.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE80_XSS__Servlet_URLConnection_68b
{
    public void badSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = CWE80_XSS__Servlet_URLConnection_68a.data;
        if (data != null)
        {
            response.getWriter().println("<br>bad(): data = " + data);
        }
    }
    public void goodG2BSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = CWE80_XSS__Servlet_URLConnection_68a.data;
        if (data != null)
        {
            response.getWriter().println("<br>bad(): data = " + data);
        }
    }
}
