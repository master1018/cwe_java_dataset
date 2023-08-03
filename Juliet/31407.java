
package testcases.CWE190_Integer_Overflow.s05;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_rand_square_66b
{
    public void badSink(short dataArray[] ) throws Throwable
    {
        short data = dataArray[2];
        short result = (short)(data * data);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(short dataArray[] ) throws Throwable
    {
        short data = dataArray[2];
        short result = (short)(data * data);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(short dataArray[] ) throws Throwable
    {
        short data = dataArray[2];
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
