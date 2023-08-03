
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_console_readLine_square_67b
{
    public void badSink(CWE190_Integer_Overflow__short_console_readLine_square_67a.Container dataContainer ) throws Throwable
    {
        short data = dataContainer.containerOne;
        short result = (short)(data * data);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(CWE190_Integer_Overflow__short_console_readLine_square_67a.Container dataContainer ) throws Throwable
    {
        short data = dataContainer.containerOne;
        short result = (short)(data * data);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(CWE190_Integer_Overflow__short_console_readLine_square_67a.Container dataContainer ) throws Throwable
    {
        short data = dataContainer.containerOne;
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
