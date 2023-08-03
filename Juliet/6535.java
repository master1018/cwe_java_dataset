
package testcases.CWE601_Open_Redirect;
import testcasesupport.*;
import javax.servlet.http.*;
import java.net.URI;
import java.net.URISyntaxException;
public class CWE601_Open_Redirect__Servlet_getQueryString_Servlet_81_goodG2B extends CWE601_Open_Redirect__Servlet_getQueryString_Servlet_81_base
{
    public void action(String data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (data != null)
        {
            URI uri;
            try
            {
                uri = new URI(data);
            }
            catch (URISyntaxException exceptURISyntax)
            {
                response.getWriter().write("Invalid redirect URL");
                return;
            }
            response.sendRedirect(data);
            return;
        }
    }
}
