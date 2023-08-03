
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_console_readLine_square_68b
{
    public void badSink() throws Throwable
    {
        short data = CWE190_Integer_Overflow__short_console_readLine_square_68a.data;
        short result = (short)(data * data);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink() throws Throwable
    {
        short data = CWE190_Integer_Overflow__short_console_readLine_square_68a.data;
        short result = (short)(data * data);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink() throws Throwable
    {
        short data = CWE190_Integer_Overflow__short_console_readLine_square_68a.data;
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
