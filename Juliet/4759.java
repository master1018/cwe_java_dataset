
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
public class CWE191_Integer_Underflow__byte_rand_multiply_67b
{
    public void badSink(CWE191_Integer_Underflow__byte_rand_multiply_67a.Container dataContainer ) throws Throwable
    {
        byte data = dataContainer.containerOne;
        if(data < 0) 
        {
            byte result = (byte)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodG2BSink(CWE191_Integer_Underflow__byte_rand_multiply_67a.Container dataContainer ) throws Throwable
    {
        byte data = dataContainer.containerOne;
        if(data < 0) 
        {
            byte result = (byte)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodB2GSink(CWE191_Integer_Underflow__byte_rand_multiply_67a.Container dataContainer ) throws Throwable
    {
        byte data = dataContainer.containerOne;
        if(data < 0) 
        {
            if (data > (Byte.MIN_VALUE/2))
            {
                byte result = (byte)(data * 2);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform multiplication.");
            }
        }
    }
}
