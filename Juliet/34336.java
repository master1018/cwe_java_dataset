
package testcases.CWE36_Absolute_Path_Traversal;
import testcasesupport.*;
import java.util.Vector;
import java.io.*;
import javax.servlet.http.*;
public class CWE36_Absolute_Path_Traversal__getParameter_Servlet_72a extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = request.getParameter("name");
        Vector<String> dataVector = new Vector<String>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE36_Absolute_Path_Traversal__getParameter_Servlet_72b()).badSink(dataVector , request, response );
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = "foo";
        Vector<String> dataVector = new Vector<String>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE36_Absolute_Path_Traversal__getParameter_Servlet_72b()).goodG2BSink(dataVector , request, response );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
