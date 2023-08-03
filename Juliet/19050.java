
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__getParameter_Servlet_trim_68b
{
    public void badSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = CWE690_NULL_Deref_From_Return__getParameter_Servlet_trim_68a.data;
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodG2BSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = CWE690_NULL_Deref_From_Return__getParameter_Servlet_trim_68a.data;
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodB2GSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = CWE690_NULL_Deref_From_Return__getParameter_Servlet_trim_68a.data;
        if (data != null)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
}
