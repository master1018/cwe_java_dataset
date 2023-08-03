
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__short_rand_sub_22b
{
    public void badSink(short data ) throws Throwable
    {
        if (CWE191_Integer_Underflow__short_rand_sub_22a.badPublicStatic)
        {
            short result = (short)(data - 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            data = 0;
        }
    }
    public void goodB2G1Sink(short data ) throws Throwable
    {
        if (CWE191_Integer_Underflow__short_rand_sub_22a.goodB2G1PublicStatic)
        {
            data = 0;
        }
        else
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
    public void goodB2G2Sink(short data ) throws Throwable
    {
        if (CWE191_Integer_Underflow__short_rand_sub_22a.goodB2G2PublicStatic)
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
        else
        {
            data = 0;
        }
    }
    public void goodG2BSink(short data ) throws Throwable
    {
        if (CWE191_Integer_Underflow__short_rand_sub_22a.goodG2BPublicStatic)
        {
            short result = (short)(data - 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            data = 0;
        }
    }
}
