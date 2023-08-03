
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__getParameter_Servlet_equals_15 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        switch (6)
        {
        case 6:
            data = request.getParameter("CWE690");
            break;
        default:
            data = null;
            break;
        }
        switch (7)
        {
        case 7:
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodG2B1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        switch (5)
        {
        case 6:
            data = null;
            break;
        default:
            data = "CWE690";
            break;
        }
        switch (7)
        {
        case 7:
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodG2B2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        switch (6)
        {
        case 6:
            data = "CWE690";
            break;
        default:
            data = null;
            break;
        }
        switch (7)
        {
        case 7:
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodB2G1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        switch (6)
        {
        case 6:
            data = request.getParameter("CWE690");
            break;
        default:
            data = null;
            break;
        }
        switch (8)
        {
        case 7:
            IO.writeLine("Benign, fixed string");
            break;
        default:
            if("CWE690".equals(data))
            {
                IO.writeLine("data is CWE690");
            }
            break;
        }
    }
    private void goodB2G2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        switch (6)
        {
        case 6:
            data = request.getParameter("CWE690");
            break;
        default:
            data = null;
            break;
        }
        switch (7)
        {
        case 7:
            if("CWE690".equals(data))
            {
                IO.writeLine("data is CWE690");
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B1(request, response);
        goodG2B2(request, response);
        goodB2G1(request, response);
        goodB2G2(request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
