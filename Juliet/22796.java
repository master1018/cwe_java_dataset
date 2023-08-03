
package testcases.CWE601_Open_Redirect;
import testcasesupport.*;
import javax.servlet.http.*;
import java.net.URI;
import java.net.URISyntaxException;
public class CWE601_Open_Redirect__Servlet_getParameter_Servlet_17 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = request.getParameter("name");
        for (int i = 0; i < 1; i++)
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
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = "foo";
        for (int i = 0; i < 1; i++)
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
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
