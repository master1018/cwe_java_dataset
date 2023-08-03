
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__short_rand_sub_54e
{
    public void badSink(short data ) throws Throwable
    {
        short result = (short)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(short data ) throws Throwable
    {
        short result = (short)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(short data ) throws Throwable
    {
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
