
package testcases.CWE80_XSS.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE80_XSS__Servlet_File_81_goodG2B extends CWE80_XSS__Servlet_File_81_base
{
    public void action(String data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (data != null)
        {
            response.getWriter().println("<br>bad(): data = " + data);
        }
    }
}
