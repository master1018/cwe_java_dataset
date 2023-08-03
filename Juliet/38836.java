
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_max_add_52c
{
    public void badSink(long data ) throws Throwable
    {
        long result = (long)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(long data ) throws Throwable
    {
        long result = (long)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(long data ) throws Throwable
    {
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
