
package testcases.CWE526_Info_Exposure_Environment_Variables;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE526_Info_Exposure_Environment_Variables__Servlet_01 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        response.getWriter().println("Not in path: " + System.getenv("PATH"));
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        good1(request, response);
    }
    private void good1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        response.getWriter().println("Not in path");
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
