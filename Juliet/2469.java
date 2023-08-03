
package testcases.CWE190_Integer_Overflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_Environment_multiply_67b
{
    public void badSink(CWE190_Integer_Overflow__int_Environment_multiply_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        if(data > 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodG2BSink(CWE190_Integer_Overflow__int_Environment_multiply_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        if(data > 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodB2GSink(CWE190_Integer_Overflow__int_Environment_multiply_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
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
