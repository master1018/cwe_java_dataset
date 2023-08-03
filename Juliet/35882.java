
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__short_min_sub_67b
{
    public void badSink(CWE191_Integer_Underflow__short_min_sub_67a.Container dataContainer ) throws Throwable
    {
        short data = dataContainer.containerOne;
        short result = (short)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(CWE191_Integer_Underflow__short_min_sub_67a.Container dataContainer ) throws Throwable
    {
        short data = dataContainer.containerOne;
        short result = (short)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(CWE191_Integer_Underflow__short_min_sub_67a.Container dataContainer ) throws Throwable
    {
        short data = dataContainer.containerOne;
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
