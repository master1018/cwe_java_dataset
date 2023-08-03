
package testcases.CWE190_Integer_Overflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_getQueryString_Servlet_add_68b
{
    public void badSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = CWE190_Integer_Overflow__int_getQueryString_Servlet_add_68a.data;
        int result = (int)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = CWE190_Integer_Overflow__int_getQueryString_Servlet_add_68a.data;
        int result = (int)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = CWE190_Integer_Overflow__int_getQueryString_Servlet_add_68a.data;
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
