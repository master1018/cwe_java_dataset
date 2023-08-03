
package testcases.CWE80_XSS.s01;
import testcasesupport.*;
import java.util.Vector;
import javax.servlet.http.*;
public class CWE80_XSS__Servlet_getQueryString_Servlet_72b
{
    public void badSink(Vector<String> dataVector , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataVector.remove(2);
        if (data != null)
        {
            response.getWriter().println("<br>bad(): data = " + data);
        }
    }
    public void goodG2BSink(Vector<String> dataVector , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataVector.remove(2);
        if (data != null)
        {
            response.getWriter().println("<br>bad(): data = " + data);
        }
    }
}
