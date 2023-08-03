
package testcases.CWE113_HTTP_Response_Splitting.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE113_HTTP_Response_Splitting__Property_setHeaderServlet_52a extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        (new CWE113_HTTP_Response_Splitting__Property_setHeaderServlet_52b()).badSink(data , request, response);
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
        (new CWE113_HTTP_Response_Splitting__Property_setHeaderServlet_52b()).goodG2BSink(data , request, response);
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        (new CWE113_HTTP_Response_Splitting__Property_setHeaderServlet_52b()).goodB2GSink(data , request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
