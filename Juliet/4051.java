
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__getParameter_Servlet_trim_61a extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = (new CWE690_NULL_Deref_From_Return__getParameter_Servlet_trim_61b()).badSource(request, response);
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = (new CWE690_NULL_Deref_From_Return__getParameter_Servlet_trim_61b()).goodG2BSource(request, response);
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = (new CWE690_NULL_Deref_From_Return__getParameter_Servlet_trim_61b()).goodB2GSource(request, response);
        if (data != null)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
