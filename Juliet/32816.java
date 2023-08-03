
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__getParameter_Servlet_trim_22b
{
    public void badSink(String data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (CWE690_NULL_Deref_From_Return__getParameter_Servlet_trim_22a.badPublicStatic)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
        else
        {
            data = null;
        }
    }
    public void goodB2G1Sink(String data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (CWE690_NULL_Deref_From_Return__getParameter_Servlet_trim_22a.goodB2G1PublicStatic)
        {
            data = null;
        }
        else
        {
            if (data != null)
            {
                String stringTrimmed = data.trim();
                IO.writeLine(stringTrimmed);
            }
        }
    }
    public void goodB2G2Sink(String data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (CWE690_NULL_Deref_From_Return__getParameter_Servlet_trim_22a.goodB2G2PublicStatic)
        {
            if (data != null)
            {
                String stringTrimmed = data.trim();
                IO.writeLine(stringTrimmed);
            }
        }
        else
        {
            data = null;
        }
    }
    public void goodG2BSink(String data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (CWE690_NULL_Deref_From_Return__getParameter_Servlet_trim_22a.goodG2BPublicStatic)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
        else
        {
            data = null;
        }
    }
}
