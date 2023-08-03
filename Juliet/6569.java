
package testcases.CWE113_HTTP_Response_Splitting.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE113_HTTP_Response_Splitting__Property_addHeaderServlet_67a extends AbstractTestCaseServlet
{
    static class Container
    {
        public String containerOne;
    }
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE113_HTTP_Response_Splitting__Property_addHeaderServlet_67b()).badSink(dataContainer , request, response );
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
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE113_HTTP_Response_Splitting__Property_addHeaderServlet_67b()).goodG2BSink(dataContainer , request, response );
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE113_HTTP_Response_Splitting__Property_addHeaderServlet_67b()).goodB2GSink(dataContainer , request, response );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
