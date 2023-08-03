
package testcases.CWE539_Information_Exposure_Through_Persistent_Cookie;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE539_Information_Exposure_Through_Persistent_Cookie__Servlet_01 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        Cookie cookie = new Cookie("SecretMessage", "test");
        cookie.setMaxAge(60*60*24*365*5);
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        good1(request, response);
    }
    private void good1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        Cookie cookie = new Cookie("SecretMessage", "test");
        cookie.setMaxAge(-1);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
