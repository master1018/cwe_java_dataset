
package testcases.CWE698_Redirect_Without_Exit;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE698_Redirect_Without_Exit__Servlet_01 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        response.sendRedirect("/test");
        IO.writeLine("doing some more things here after the redirect");
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        good1(request, response);
    }
    private void good1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        response.sendRedirect("/test");
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
