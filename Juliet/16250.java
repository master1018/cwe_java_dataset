
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__getParameter_Servlet_equals_31 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String dataCopy;
        {
            String data;
            data = request.getParameter("CWE690");
            dataCopy = data;
        }
        {
            String data = dataCopy;
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
        }
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String dataCopy;
        {
            String data;
            data = "CWE690";
            dataCopy = data;
        }
        {
            String data = dataCopy;
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
        }
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String dataCopy;
        {
            String data;
            data = request.getParameter("CWE690");
            dataCopy = data;
        }
        {
            String data = dataCopy;
            if("CWE690".equals(data))
            {
                IO.writeLine("data is CWE690");
            }
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
