
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
public class CWE191_Integer_Underflow__byte_min_sub_71b
{
    public void badSink(Object dataObject ) throws Throwable
    {
        byte data = (Byte)dataObject;
        byte result = (byte)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(Object dataObject ) throws Throwable
    {
        byte data = (Byte)dataObject;
        byte result = (byte)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(Object dataObject ) throws Throwable
    {
        byte data = (Byte)dataObject;
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
