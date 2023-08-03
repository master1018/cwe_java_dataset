
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
public class CWE191_Integer_Underflow__byte_rand_sub_68b
{
    public void badSink() throws Throwable
    {
        byte data = CWE191_Integer_Underflow__byte_rand_sub_68a.data;
        byte result = (byte)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink() throws Throwable
    {
        byte data = CWE191_Integer_Underflow__byte_rand_sub_68a.data;
        byte result = (byte)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink() throws Throwable
    {
        byte data = CWE191_Integer_Underflow__byte_rand_sub_68a.data;
        if (data > Byte.MIN_VALUE)
        {
            byte result = (byte)(data - 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too small to perform subtraction.");
        }
    }
}
