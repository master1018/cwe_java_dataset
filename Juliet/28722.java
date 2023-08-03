
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__getParameter_Servlet_equals_13 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        if (IO.STATIC_FINAL_FIVE==5)
        {
            data = request.getParameter("CWE690");
        }
        else
        {
            data = null;
        }
        if (IO.STATIC_FINAL_FIVE==5)
        {
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
        }
    }
    private void goodG2B1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        if (IO.STATIC_FINAL_FIVE!=5)
        {
            data = null;
        }
        else
        {
            data = "CWE690";
        }
        if (IO.STATIC_FINAL_FIVE==5)
        {
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
        }
    }
    private void goodG2B2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        if (IO.STATIC_FINAL_FIVE==5)
        {
            data = "CWE690";
        }
        else
        {
            data = null;
        }
        if (IO.STATIC_FINAL_FIVE==5)
        {
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
        }
    }
    private void goodB2G1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        if (IO.STATIC_FINAL_FIVE==5)
        {
            data = request.getParameter("CWE690");
        }
        else
        {
            data = null;
        }
        if (IO.STATIC_FINAL_FIVE!=5)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if("CWE690".equals(data))
            {
                IO.writeLine("data is CWE690");
            }
        }
    }
    private void goodB2G2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        if (IO.STATIC_FINAL_FIVE==5)
        {
            data = request.getParameter("CWE690");
        }
        else
        {
            data = null;
        }
        if (IO.STATIC_FINAL_FIVE==5)
        {
            if("CWE690".equals(data))
            {
                IO.writeLine("data is CWE690");
            }
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
