
package testcases.CWE89_SQL_Injection.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE89_SQL_Injection__getCookies_Servlet_prepareStatement_81a extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = ""; 
        {
            Cookie cookieSources[] = request.getCookies();
            if (cookieSources != null)
            {
                data = cookieSources[0].getValue();
            }
        }
        CWE89_SQL_Injection__getCookies_Servlet_prepareStatement_81_base baseObject = new CWE89_SQL_Injection__getCookies_Servlet_prepareStatement_81_bad();
        baseObject.action(data , request, response);
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = "foo";
        CWE89_SQL_Injection__getCookies_Servlet_prepareStatement_81_base baseObject = new CWE89_SQL_Injection__getCookies_Servlet_prepareStatement_81_goodG2B();
        baseObject.action(data , request, response);
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = ""; 
        {
            Cookie cookieSources[] = request.getCookies();
            if (cookieSources != null)
            {
                data = cookieSources[0].getValue();
            }
        }
        CWE89_SQL_Injection__getCookies_Servlet_prepareStatement_81_base baseObject = new CWE89_SQL_Injection__getCookies_Servlet_prepareStatement_81_goodB2G();
        baseObject.action(data , request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
