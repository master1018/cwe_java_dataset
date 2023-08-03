
package testcases.CWE643_Xpath_Injection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE643_Xpath_Injection__getParameter_Servlet_68a extends AbstractTestCaseServlet
{
    public static String data;
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        data = request.getParameter("name");
        (new CWE643_Xpath_Injection__getParameter_Servlet_68b()).badSink(request, response);
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        data = "foo";
        (new CWE643_Xpath_Injection__getParameter_Servlet_68b()).goodG2BSink(request, response);
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        data = request.getParameter("name");
        (new CWE643_Xpath_Injection__getParameter_Servlet_68b()).goodB2GSink(request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
