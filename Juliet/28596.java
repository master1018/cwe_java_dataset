
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__long_rand_sub_22b
{
    public void badSink(long data ) throws Throwable
    {
        if (CWE191_Integer_Underflow__long_rand_sub_22a.badPublicStatic)
        {
            long result = (long)(data - 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            data = 0L;
        }
    }
    public void goodB2G1Sink(long data ) throws Throwable
    {
        if (CWE191_Integer_Underflow__long_rand_sub_22a.goodB2G1PublicStatic)
        {
            data = 0L;
        }
        else
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
    public void goodB2G2Sink(long data ) throws Throwable
    {
        if (CWE191_Integer_Underflow__long_rand_sub_22a.goodB2G2PublicStatic)
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
        else
        {
            data = 0L;
        }
    }
    public void goodG2BSink(long data ) throws Throwable
    {
        if (CWE191_Integer_Underflow__long_rand_sub_22a.goodG2BPublicStatic)
        {
            long result = (long)(data - 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            data = 0L;
        }
    }
}
