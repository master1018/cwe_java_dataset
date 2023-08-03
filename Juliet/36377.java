
package testcases.CWE382_Use_of_System_Exit;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE382_Use_of_System_Exit__Servlet_System_17 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        for(int j = 0; j < 1; j++)
        {
            System.exit(1);
        }
    }
    private void good1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        for(int k = 0; k < 1; k++)
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
