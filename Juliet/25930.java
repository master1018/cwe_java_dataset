
package testcases.CWE190_Integer_Overflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_getParameter_Servlet_add_67b
{
    public void badSink(CWE190_Integer_Overflow__int_getParameter_Servlet_add_67a.Container dataContainer , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataContainer.containerOne;
        int result = (int)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(CWE190_Integer_Overflow__int_getParameter_Servlet_add_67a.Container dataContainer , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataContainer.containerOne;
        int result = (int)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(CWE190_Integer_Overflow__int_getParameter_Servlet_add_67a.Container dataContainer , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataContainer.containerOne;
        if (data < Integer.MAX_VALUE)
        {
            int result = (int)(data + 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too large to perform addition.");
        }
    }
}
