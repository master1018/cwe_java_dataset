
package testcases.CWE382_Use_of_System_Exit;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE382_Use_of_System_Exit__Servlet_Runtime_01 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        Runtime.getRuntime().exit(1);
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        good1(request, response);
    }
    private void good1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        response.getWriter().write("You cannot shut down this application, only the admin can");
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
