
package testcases.CWE190_Integer_Overflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_getQueryString_Servlet_multiply_68b
{
    public void badSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = CWE190_Integer_Overflow__int_getQueryString_Servlet_multiply_68a.data;
        if(data > 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodG2BSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = CWE190_Integer_Overflow__int_getQueryString_Servlet_multiply_68a.data;
        if(data > 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodB2GSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = CWE190_Integer_Overflow__int_getQueryString_Servlet_multiply_68a.data;
        if(data > 0) 
        {
            if (data < (Integer.MAX_VALUE/2))
            {
                int result = (int)(data * 2);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform multiplication.");
            }
        }
    }
}