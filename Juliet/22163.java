
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__getParameter_Servlet_equals_12 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = request.getParameter("CWE690");
        }
        else
        {
            data = "CWE690";
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
        }
        else
        {
            if("CWE690".equals(data))
            {
                IO.writeLine("data is CWE690");
            }
        }
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = "CWE690";
        }
        else
        {
            data = "CWE690";
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
        }
        else
        {
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
        }
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = request.getParameter("CWE690");
        }
        else
        {
            data = request.getParameter("CWE690");
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            if("CWE690".equals(data))
            {
                IO.writeLine("data is CWE690");
            }
        }
        else
        {
            if("CWE690".equals(data))
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
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
