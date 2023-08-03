
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__getParameter_Servlet_trim_81_goodG2B extends CWE690_NULL_Deref_From_Return__getParameter_Servlet_trim_81_base
{
    public void action(String data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
}
