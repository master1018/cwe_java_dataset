
package testcases.CWE566_Authorization_Bypass_Through_SQL_Primary;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE566_Authorization_Bypass_Through_SQL_Primary__Servlet_67a extends AbstractTestCaseServlet
{
    static class Container
    {
        public String containerOne;
    }
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = request.getParameter("id");
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE566_Authorization_Bypass_Through_SQL_Primary__Servlet_67b()).badSink(dataContainer , request, response );
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = "10";
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE566_Authorization_Bypass_Through_SQL_Primary__Servlet_67b()).goodG2BSink(dataContainer , request, response );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
