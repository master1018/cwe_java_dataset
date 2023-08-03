
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_console_readLine_add_67b
{
    public void badSink(CWE190_Integer_Overflow__long_console_readLine_add_67a.Container dataContainer ) throws Throwable
    {
        long data = dataContainer.containerOne;
        long result = (long)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(CWE190_Integer_Overflow__long_console_readLine_add_67a.Container dataContainer ) throws Throwable
    {
        long data = dataContainer.containerOne;
        long result = (long)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(CWE190_Integer_Overflow__long_console_readLine_add_67a.Container dataContainer ) throws Throwable
    {
        long data = dataContainer.containerOne;
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
