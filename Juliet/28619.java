
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__long_rand_sub_67b
{
    public void badSink(CWE191_Integer_Underflow__long_rand_sub_67a.Container dataContainer ) throws Throwable
    {
        long data = dataContainer.containerOne;
        long result = (long)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(CWE191_Integer_Underflow__long_rand_sub_67a.Container dataContainer ) throws Throwable
    {
        long data = dataContainer.containerOne;
        long result = (long)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(CWE191_Integer_Underflow__long_rand_sub_67a.Container dataContainer ) throws Throwable
    {
        long data = dataContainer.containerOne;
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
