
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE191_Integer_Underflow__int_Environment_multiply_68b
{
    public void badSink() throws Throwable
    {
        int data = CWE191_Integer_Underflow__int_Environment_multiply_68a.data;
        if(data < 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodG2BSink() throws Throwable
    {
        int data = CWE191_Integer_Underflow__int_Environment_multiply_68a.data;
        if(data < 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodB2GSink() throws Throwable
    {
        int data = CWE191_Integer_Underflow__int_Environment_multiply_68a.data;
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
