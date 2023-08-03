
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
public class CWE191_Integer_Underflow__byte_console_readLine_sub_52c
{
    public void badSink(byte data ) throws Throwable
    {
        byte result = (byte)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(byte data ) throws Throwable
    {
        byte result = (byte)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(byte data ) throws Throwable
    {
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
