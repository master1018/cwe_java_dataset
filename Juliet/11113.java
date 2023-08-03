
package testcases.CWE382_Use_of_System_Exit;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE382_Use_of_System_Exit__Servlet_System_12 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (IO.staticReturnsTrueOrFalse())
        {
            System.exit(1);
        }
        else
        {
            response.getWriter().write("You cannot shut down this application, only the admin can");
        }
    }
    private void good1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (IO.staticReturnsTrueOrFalse())
        {
            response.getWriter().write("You cannot shut down this application, only the admin can");
        }
        else
        {
            response.getWriter().write("You cannot shut down this application, only the admin can");
        }
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        good1(request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
