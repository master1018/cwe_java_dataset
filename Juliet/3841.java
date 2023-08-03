
package testcases.CWE80_XSS.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE80_XSS__Servlet_getParameter_Servlet_22a extends AbstractTestCaseServlet
{
    public static boolean badPublicStatic = false;
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        badPublicStatic = true;
        data = (new CWE80_XSS__Servlet_getParameter_Servlet_22b()).badSource(request, response);
        if (data != null)
        {
            response.getWriter().println("<br>bad(): data = " + data);
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
        data = (new CWE80_XSS__Servlet_getParameter_Servlet_22b()).goodG2B1Source(request, response);
        if (data != null)
        {
            response.getWriter().println("<br>bad(): data = " + data);
        }
    }
    private void goodG2B2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        goodG2B2PublicStatic = true;
        data = (new CWE80_XSS__Servlet_getParameter_Servlet_22b()).goodG2B2Source(request, response);
        if (data != null)
        {
            response.getWriter().println("<br>bad(): data = " + data);
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
