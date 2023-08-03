
package testcases.CWE601_Open_Redirect;
import testcasesupport.*;
import javax.servlet.http.*;
import java.net.URI;
import java.net.URISyntaxException;
public class CWE601_Open_Redirect__Servlet_URLConnection_22a extends AbstractTestCaseServlet
{
    public static boolean badPublicStatic = false;
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        badPublicStatic = true;
        data = (new CWE601_Open_Redirect__Servlet_URLConnection_22b()).badSource(request, response);
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
    public static boolean goodG2B1PublicStatic = false;
    public static boolean goodG2B2PublicStatic = false;
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B1(request, response);
        goodG2B2(request, response);
    }
    private void goodG2B1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        goodG2B1PublicStatic = false;
        data = (new CWE601_Open_Redirect__Servlet_URLConnection_22b()).goodG2B1Source(request, response);
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
    private void goodG2B2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        goodG2B2PublicStatic = true;
        data = (new CWE601_Open_Redirect__Servlet_URLConnection_22b()).goodG2B2Source(request, response);
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
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
