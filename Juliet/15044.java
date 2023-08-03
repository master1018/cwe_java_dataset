
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__getParameter_Servlet_equals_81_goodB2G extends CWE690_NULL_Deref_From_Return__getParameter_Servlet_equals_81_base
{
    public void action(String data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if("CWE690".equals(data))
        {
            IO.writeLine("data is CWE690");
        }
    }
}
