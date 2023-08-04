
package testcases.CWE614_Sensitive_Cookie_Without_Secure;
import testcasesupport.*;
import javax.servlet.http.*;
import java.io.*;
public class CWE614_Sensitive_Cookie_Without_Secure__Servlet_16 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        while(true)
        {
            Cookie cookie = new Cookie("SecretMessage", "test");
            if (request.isSecure())
            {
                response.addCookie(cookie);
            }
            break;
        }
    }
    private void good1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        while(true)
        {
            Cookie cookie = new Cookie("SecretMessage", "Drink your Ovaltine");
            if (request.isSecure())
            {
                cookie.setSecure(true);
                response.addCookie(cookie);
            }
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