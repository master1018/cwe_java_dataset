
package testcases.CWE477_Obsolete_Functions;
import testcasesupport.*;
import java.net.URLEncoder;
import javax.servlet.http.*;
public class CWE477_Obsolete_Functions__URLEncoder_encode_Servlet_15 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        switch (7)
        {
        case 7:
            response.getWriter().println(URLEncoder.encode("abc|1 $#@<><()"));
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void good1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        switch (8)
        {
        case 7:
            IO.writeLine("Benign, fixed string");
            break;
        default:
            response.getWriter().println(URLEncoder.encode("abc|1 $#@<><()", "UTF-8"));
            break;
        }
    }
    private void good2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        switch (7)
        {
        case 7:
            response.getWriter().println(URLEncoder.encode("abc|1 $#@<><()", "UTF-8"));
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
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
