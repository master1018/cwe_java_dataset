
package testcases.CWE566_Authorization_Bypass_Through_SQL_Primary;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE566_Authorization_Bypass_Through_SQL_Primary__Servlet_66a extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = request.getParameter("id");
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE566_Authorization_Bypass_Through_SQL_Primary__Servlet_66b()).badSink(dataArray , request, response );
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = "10";
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE566_Authorization_Bypass_Through_SQL_Primary__Servlet_66b()).goodG2BSink(dataArray , request, response );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
