
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
public class CWE191_Integer_Underflow__byte_console_readLine_sub_67b
{
    public void badSink(CWE191_Integer_Underflow__byte_console_readLine_sub_67a.Container dataContainer ) throws Throwable
    {
        byte data = dataContainer.containerOne;
        byte result = (byte)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(CWE191_Integer_Underflow__byte_console_readLine_sub_67a.Container dataContainer ) throws Throwable
    {
        byte data = dataContainer.containerOne;
        byte result = (byte)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(CWE191_Integer_Underflow__byte_console_readLine_sub_67a.Container dataContainer ) throws Throwable
    {
        byte data = dataContainer.containerOne;
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
