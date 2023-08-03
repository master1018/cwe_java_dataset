
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__getParameter_Servlet_equals_16 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        while (true)
        {
            data = request.getParameter("CWE690");
            break;
        }
        while (true)
        {
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
            break;
        }
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        while (true)
        {
            data = "CWE690";
            break;
        }
        while (true)
        {
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
            break;
        }
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        while (true)
        {
            data = request.getParameter("CWE690");
            break;
        }
        while (true)
        {
            if("CWE690".equals(data))
            {
                IO.writeLine("data is CWE690");
            }
            break;
        }
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
