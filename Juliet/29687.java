
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
import java.util.Vector;
public class CWE191_Integer_Underflow__short_rand_sub_72b
{
    public void badSink(Vector<Short> dataVector ) throws Throwable
    {
        short data = dataVector.remove(2);
        short result = (short)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(Vector<Short> dataVector ) throws Throwable
    {
        short data = dataVector.remove(2);
        short result = (short)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(Vector<Short> dataVector ) throws Throwable
    {
        short data = dataVector.remove(2);
        if (data > Short.MIN_VALUE)
        {
            short result = (short)(data - 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too small to perform subtraction.");
        }
    }
}
