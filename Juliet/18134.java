
package testcases.CWE113_HTTP_Response_Splitting.s01;
import testcasesupport.*;
import java.util.Vector;
import javax.servlet.http.*;
public class CWE113_HTTP_Response_Splitting__Environment_addCookieServlet_72a extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        Vector<String> dataVector = new Vector<String>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE113_HTTP_Response_Splitting__Environment_addCookieServlet_72b()).badSink(dataVector , request, response );
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
        Vector<String> dataVector = new Vector<String>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE113_HTTP_Response_Splitting__Environment_addCookieServlet_72b()).goodG2BSink(dataVector , request, response );
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        Vector<String> dataVector = new Vector<String>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE113_HTTP_Response_Splitting__Environment_addCookieServlet_72b()).goodB2GSink(dataVector , request, response );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
