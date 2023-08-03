
package testcases.CWE190_Integer_Overflow.s05;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_max_square_54e
{
    public void badSink(short data ) throws Throwable
    {
        short result = (short)(data * data);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(short data ) throws Throwable
    {
        short result = (short)(data * data);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(short data ) throws Throwable
    {
        if ((data != Integer.MIN_VALUE) && (data != Long.MIN_VALUE) && (Math.abs(data) <= (long)Math.sqrt(Short.MAX_VALUE)))
        {
            short result = (short)(data * data);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too large to perform squaring.");
        }
    }
}
