
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
public class CWE191_Integer_Underflow__byte_rand_sub_66b
{
    public void badSink(byte dataArray[] ) throws Throwable
    {
        byte data = dataArray[2];
        byte result = (byte)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(byte dataArray[] ) throws Throwable
    {
        byte data = dataArray[2];
        byte result = (byte)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(byte dataArray[] ) throws Throwable
    {
        byte data = dataArray[2];
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
