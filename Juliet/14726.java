
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__long_rand_multiply_22b
{
    public void badSink(long data ) throws Throwable
    {
        if (CWE191_Integer_Underflow__long_rand_multiply_22a.badPublicStatic)
        {
            if(data < 0) 
            {
                long result = (long)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
        else
        {
            data = 0L;
        }
    }
    public void goodB2G1Sink(long data ) throws Throwable
    {
        if (CWE191_Integer_Underflow__long_rand_multiply_22a.goodB2G1PublicStatic)
        {
            data = 0L;
        }
        else
        {
            if(data < 0) 
            {
                if (data > (Long.MIN_VALUE/2))
                {
                    long result = (long)(data * 2);
                    IO.writeLine("result: " + result);
                }
                else
                {
                    IO.writeLine("data value is too small to perform multiplication.");
                }
            }
        }
    }
    public void goodB2G2Sink(long data ) throws Throwable
    {
        if (CWE191_Integer_Underflow__long_rand_multiply_22a.goodB2G2PublicStatic)
        {
            if(data < 0) 
            {
                if (data > (Long.MIN_VALUE/2))
                {
                    long result = (long)(data * 2);
                    IO.writeLine("result: " + result);
                }
                else
                {
                    IO.writeLine("data value is too small to perform multiplication.");
                }
            }
        }
        else
        {
            data = 0L;
        }
    }
    public void goodG2BSink(long data ) throws Throwable
    {
        if (CWE191_Integer_Underflow__long_rand_multiply_22a.goodG2BPublicStatic)
        {
            if(data < 0) 
            {
                long result = (long)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
        else
        {
            data = 0L;
        }
    }
}
