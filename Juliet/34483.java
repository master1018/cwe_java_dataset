
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__getParameter_Servlet_equals_68b
{
    public void badSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = CWE690_NULL_Deref_From_Return__getParameter_Servlet_equals_68a.data;
        if(data.equals("CWE690"))
        {
            IO.writeLine("data is CWE690");
        }
    }
    public void goodG2BSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = CWE690_NULL_Deref_From_Return__getParameter_Servlet_equals_68a.data;
        if(data.equals("CWE690"))
        {
            IO.writeLine("data is CWE690");
        }
    }
    public void goodB2GSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = CWE690_NULL_Deref_From_Return__getParameter_Servlet_equals_68a.data;
        if("CWE690".equals(data))
        {
            IO.writeLine("data is CWE690");
        }
    }
}
