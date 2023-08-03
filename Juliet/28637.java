
package testcases.CWE643_Xpath_Injection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE643_Xpath_Injection__getParameter_Servlet_22a extends AbstractTestCaseServlet
{
    public static boolean badPublicStatic = false;
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = null;
        data = request.getParameter("name");
        badPublicStatic = true;
        (new CWE643_Xpath_Injection__getParameter_Servlet_22b()).badSink(data , request, response);
    }
    public static boolean goodB2G1PublicStatic = false;
    public static boolean goodB2G2PublicStatic = false;
    public static boolean goodG2BPublicStatic = false;
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodB2G1(request, response);
        goodB2G2(request, response);
        goodG2B(request, response);
    }
    private void goodB2G1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = null;
        data = request.getParameter("name");
        goodB2G1PublicStatic = false;
        (new CWE643_Xpath_Injection__getParameter_Servlet_22b()).goodB2G1Sink(data , request, response);
    }
    private void goodB2G2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = null;
        data = request.getParameter("name");
        goodB2G2PublicStatic = true;
        (new CWE643_Xpath_Injection__getParameter_Servlet_22b()).goodB2G2Sink(data , request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = null;
        data = "foo";
        goodG2BPublicStatic = true;
        (new CWE643_Xpath_Injection__getParameter_Servlet_22b()).goodG2BSink(data , request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
