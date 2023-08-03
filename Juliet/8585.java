
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
import java.util.Vector;
public class CWE191_Integer_Underflow__byte_min_sub_72b
{
    public void badSink(Vector<Byte> dataVector ) throws Throwable
    {
        byte data = dataVector.remove(2);
        byte result = (byte)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(Vector<Byte> dataVector ) throws Throwable
    {
        byte data = dataVector.remove(2);
        byte result = (byte)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(Vector<Byte> dataVector ) throws Throwable
    {
        byte data = dataVector.remove(2);
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
