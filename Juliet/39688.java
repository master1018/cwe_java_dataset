
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__short_rand_sub_68b
{
    public void badSink() throws Throwable
    {
        short data = CWE191_Integer_Underflow__short_rand_sub_68a.data;
        short result = (short)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink() throws Throwable
    {
        short data = CWE191_Integer_Underflow__short_rand_sub_68a.data;
        short result = (short)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink() throws Throwable
    {
        short data = CWE191_Integer_Underflow__short_rand_sub_68a.data;
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
