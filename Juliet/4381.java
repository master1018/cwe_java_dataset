
package testcases.CWE83_XSS_Attribute;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE83_XSS_Attribute__Servlet_database_81_bad extends CWE83_XSS_Attribute__Servlet_database_81_base
{
    public void action(String data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (data != null)
        {
            response.getWriter().println("<br>bad() - <img src=\"" + data + "\">");
        }
    }
}
