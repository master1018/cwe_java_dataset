
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_max_square_22b
{
    public void badSink(long data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__long_max_square_22a.badPublicStatic)
        {
            long result = (long)(data * data);
            IO.writeLine("result: " + result);
        }
        else
        {
            data = 0L;
        }
    }
    public void goodB2G1Sink(long data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__long_max_square_22a.goodB2G1PublicStatic)
        {
            data = 0L;
        }
        else
        {
            if ((data != Integer.MIN_VALUE) && (data != Long.MIN_VALUE) && (Math.abs(data) <= (long)Math.sqrt(Long.MAX_VALUE)))
            {
                long result = (long)(data * data);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform squaring.");
            }
        }
    }
    public void goodB2G2Sink(long data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__long_max_square_22a.goodB2G2PublicStatic)
        {
            if ((data != Integer.MIN_VALUE) && (data != Long.MIN_VALUE) && (Math.abs(data) <= (long)Math.sqrt(Long.MAX_VALUE)))
            {
                long result = (long)(data * data);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform squaring.");
            }
        }
        else
        {
            data = 0L;
        }
    }
    public void goodG2BSink(long data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__long_max_square_22a.goodG2BPublicStatic)
        {
            long result = (long)(data * data);
            IO.writeLine("result: " + result);
        }
        else
        {
            data = 0L;
        }
    }
}
