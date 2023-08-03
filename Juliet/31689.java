
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__getParameter_Servlet_equals_66a extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = request.getParameter("CWE690");
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE690_NULL_Deref_From_Return__getParameter_Servlet_equals_66b()).badSink(dataArray , request, response );
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = "CWE690";
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE690_NULL_Deref_From_Return__getParameter_Servlet_equals_66b()).goodG2BSink(dataArray , request, response );
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = request.getParameter("CWE690");
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE690_NULL_Deref_From_Return__getParameter_Servlet_equals_66b()).goodB2GSink(dataArray , request, response );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
