
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__long_rand_sub_52c
{
    public void badSink(long data ) throws Throwable
    {
        long result = (long)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(long data ) throws Throwable
    {
        long result = (long)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(long data ) throws Throwable
    {
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
