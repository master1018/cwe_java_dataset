
package testcases.CWE15_External_Control_of_System_or_Configuration_Setting;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE15_External_Control_of_System_or_Configuration_Setting__getParameter_Servlet_81a extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = request.getParameter("name");
        CWE15_External_Control_of_System_or_Configuration_Setting__getParameter_Servlet_81_base baseObject = new CWE15_External_Control_of_System_or_Configuration_Setting__getParameter_Servlet_81_bad();
        baseObject.action(data , request, response);
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = "foo";
        CWE15_External_Control_of_System_or_Configuration_Setting__getParameter_Servlet_81_base baseObject = new CWE15_External_Control_of_System_or_Configuration_Setting__getParameter_Servlet_81_goodG2B();
        baseObject.action(data , request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
