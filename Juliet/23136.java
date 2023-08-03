
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_max_square_52c
{
    public void badSink(long data ) throws Throwable
    {
        long result = (long)(data * data);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(long data ) throws Throwable
    {
        long result = (long)(data * data);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(long data ) throws Throwable
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
