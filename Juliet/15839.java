
package testcases.CWE190_Integer_Overflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_getQueryString_Servlet_multiply_22b
{
    public void badSink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (CWE190_Integer_Overflow__int_getQueryString_Servlet_multiply_22a.badPublicStatic)
        {
            if(data > 0) 
            {
                int result = (int)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
        else
        {
            data = 0;
        }
    }
    public void goodB2G1Sink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (CWE190_Integer_Overflow__int_getQueryString_Servlet_multiply_22a.goodB2G1PublicStatic)
        {
            data = 0;
        }
        else
        {
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
    public void goodB2G2Sink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (CWE190_Integer_Overflow__int_getQueryString_Servlet_multiply_22a.goodB2G2PublicStatic)
        {
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
        else
        {
            data = 0;
        }
    }
    public void goodG2BSink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (CWE190_Integer_Overflow__int_getQueryString_Servlet_multiply_22a.goodG2BPublicStatic)
        {
            if(data > 0) 
            {
                int result = (int)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
        else
        {
            data = 0;
        }
    }
}
