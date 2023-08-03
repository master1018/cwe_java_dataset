
package testcases.CWE78_OS_Command_Injection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE78_OS_Command_Injection__getCookies_Servlet_67a extends AbstractTestCaseServlet
{
    static class Container
    {
        public String containerOne;
    }
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
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE78_OS_Command_Injection__getCookies_Servlet_67b()).badSink(dataContainer , request, response );
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = "foo";
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE78_OS_Command_Injection__getCookies_Servlet_67b()).goodG2BSink(dataContainer , request, response );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
