
package testcases.CWE615_Info_Exposure_by_Comment;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE615_Info_Exposure_by_Comment__Servlet_01 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        response.getWriter().println("<!--DB username = joe, DB password = 123-->" +
        "<form action=\"/test\" method=post>" +
        "<input type=text name=dbusername>" +
        "<input type=test name=dbpassword>" +
        "<input type=submit value=Submit>" +
        "</form>");
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        good1(request, response);
    }
    private void good1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        response.getWriter().println("<form action=\"/test\" method=post>" +
        "<input type=text name=dbusername>" +
        "<input type=test name=dbpassword>" +
        "<input type=submit value=Submit>" +
        "</form>");
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
