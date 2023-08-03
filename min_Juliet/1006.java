
package testcases.CWE698_Redirect_Without_Exit;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE698_Redirect_Without_Exit__Servlet_11 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (IO.staticReturnsTrue())
        {
            response.sendRedirect("/test");
            IO.writeLine("doing some more things here after the redirect");
        }
    }
    private void good1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (IO.staticReturnsFalse())
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            response.sendRedirect("/test");
        }
    }
    private void good2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (IO.staticReturnsTrue())
        {
            response.sendRedirect("/test");
        }
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        good1(request, response);
        good2(request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
