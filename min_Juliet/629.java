
package testcases.CWE382_Use_of_System_Exit;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE382_Use_of_System_Exit__Servlet_System_10 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (IO.staticTrue)
        {
            System.exit(1);
        }
    }
    private void good1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (IO.staticFalse)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            response.getWriter().write("You cannot shut down this application, only the admin can");
        }
    }
    private void good2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (IO.staticTrue)
        {
            response.getWriter().write("You cannot shut down this application, only the admin can");
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