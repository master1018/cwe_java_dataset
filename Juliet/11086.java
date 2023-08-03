
package testcases.CWE698_Redirect_Without_Exit;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE698_Redirect_Without_Exit__Servlet_15 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        switch (7)
        {
        case 7:
            response.sendRedirect("/test");
            IO.writeLine("doing some more things here after the redirect");
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void good1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        switch (8)
        {
        case 7:
            IO.writeLine("Benign, fixed string");
            break;
        default:
            response.sendRedirect("/test");
            break;
        }
    }
    private void good2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        switch (7)
        {
        case 7:
            response.sendRedirect("/test");
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
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
