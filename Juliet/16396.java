
package testcases.CWE80_XSS.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE80_XSS__Servlet_database_71b
{
    public void badSink(Object dataObject , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = (String)dataObject;
        if (data != null)
        {
            response.getWriter().println("<br>bad(): data = " + data);
        }
    }
    public void goodG2BSink(Object dataObject , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = (String)dataObject;
        if (data != null)
        {
            response.getWriter().println("<br>bad(): data = " + data);
        }
    }
}
