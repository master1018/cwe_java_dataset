
package testcases.CWE191_Integer_Underflow.s02;
import testcasesupport.*;
import java.util.Vector;
import javax.servlet.http.*;
public class CWE191_Integer_Underflow__int_getQueryString_Servlet_multiply_72b
{
    public void badSink(Vector<Integer> dataVector , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataVector.remove(2);
        if(data < 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodG2BSink(Vector<Integer> dataVector , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataVector.remove(2);
        if(data < 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodB2GSink(Vector<Integer> dataVector , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataVector.remove(2);
        if(data < 0) 
        {
            if (data > (Integer.MIN_VALUE/2))
            {
                int result = (int)(data * 2);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform multiplication.");
            }
        }
    }
}
