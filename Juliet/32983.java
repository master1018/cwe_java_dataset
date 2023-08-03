
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_max_square_71b
{
    public void badSink(Object dataObject ) throws Throwable
    {
        long data = (Long)dataObject;
        long result = (long)(data * data);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(Object dataObject ) throws Throwable
    {
        long data = (Long)dataObject;
        long result = (long)(data * data);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(Object dataObject ) throws Throwable
    {
        long data = (Long)dataObject;
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
