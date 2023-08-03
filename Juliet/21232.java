
package testcases.CWE539_Information_Exposure_Through_Persistent_Cookie;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE539_Information_Exposure_Through_Persistent_Cookie__Servlet_07 extends AbstractTestCaseServlet
{
    private int privateFive = 5;
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (privateFive == 5)
        {
            Cookie cookie = new Cookie("SecretMessage", "test");
            cookie.setMaxAge(60*60*24*365*5);
        }
    }
    private void good1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (privateFive != 5)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            Cookie cookie = new Cookie("SecretMessage", "test");
            cookie.setMaxAge(-1);
        }
    }
    private void good2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (privateFive == 5)
        {
            Cookie cookie = new Cookie("SecretMessage", "test");
            cookie.setMaxAge(-1);
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
