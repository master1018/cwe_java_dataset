
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_rand_add_22b
{
    public void badSink(long data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__long_rand_add_22a.badPublicStatic)
        {
            long result = (long)(data + 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            data = 0L;
        }
    }
    public void goodB2G1Sink(long data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__long_rand_add_22a.goodB2G1PublicStatic)
        {
            data = 0L;
        }
        else
        {
            if (data < Long.MAX_VALUE)
            {
                long result = (long)(data + 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform addition.");
            }
        }
    }
    public void goodB2G2Sink(long data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__long_rand_add_22a.goodB2G2PublicStatic)
        {
            if (data < Long.MAX_VALUE)
            {
                long result = (long)(data + 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform addition.");
            }
        }
        else
        {
            data = 0L;
        }
    }
    public void goodG2BSink(long data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__long_rand_add_22a.goodG2BPublicStatic)
        {
            long result = (long)(data + 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            data = 0L;
        }
    }
}
