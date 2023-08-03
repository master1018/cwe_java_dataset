
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_console_readLine_add_71b
{
    public void badSink(Object dataObject ) throws Throwable
    {
        long data = (Long)dataObject;
        long result = (long)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(Object dataObject ) throws Throwable
    {
        long data = (Long)dataObject;
        long result = (long)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(Object dataObject ) throws Throwable
    {
        long data = (Long)dataObject;
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
