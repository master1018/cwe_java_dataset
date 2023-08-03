
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_console_readLine_add_68b
{
    public void badSink() throws Throwable
    {
        short data = CWE190_Integer_Overflow__short_console_readLine_add_68a.data;
        short result = (short)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink() throws Throwable
    {
        short data = CWE190_Integer_Overflow__short_console_readLine_add_68a.data;
        short result = (short)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink() throws Throwable
    {
        short data = CWE190_Integer_Overflow__short_console_readLine_add_68a.data;
        if (data < Short.MAX_VALUE)
        {
            short result = (short)(data + 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too large to perform addition.");
        }
    }
}
