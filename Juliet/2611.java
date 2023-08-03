
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
import java.util.Vector;
public class CWE191_Integer_Underflow__long_console_readLine_sub_72b
{
    public void badSink(Vector<Long> dataVector ) throws Throwable
    {
        long data = dataVector.remove(2);
        long result = (long)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(Vector<Long> dataVector ) throws Throwable
    {
        long data = dataVector.remove(2);
        long result = (long)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(Vector<Long> dataVector ) throws Throwable
    {
        long data = dataVector.remove(2);
        if (data > Long.MIN_VALUE)
        {
            long result = (long)(data - 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too small to perform subtraction.");
        }
    }
}
