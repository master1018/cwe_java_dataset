
package testcases.CWE15_External_Control_of_System_or_Configuration_Setting;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE15_External_Control_of_System_or_Configuration_Setting__getParameter_Servlet_68a extends AbstractTestCaseServlet
{
    public static String data;
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        data = request.getParameter("name");
        (new CWE15_External_Control_of_System_or_Configuration_Setting__getParameter_Servlet_68b()).badSink(request, response);
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        data = "foo";
        (new CWE15_External_Control_of_System_or_Configuration_Setting__getParameter_Servlet_68b()).goodG2BSink(request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}