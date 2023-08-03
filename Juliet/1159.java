
package testcases.CWE526_Info_Exposure_Environment_Variables;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE526_Info_Exposure_Environment_Variables__Servlet_16 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        while(true)
        {
            response.getWriter().println("Not in path: " + System.getenv("PATH"));
            break;
        }
    }
    private void good1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        while(true)
        {
            response.getWriter().println("Not in path");
            break;
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
