
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_rand_add_68b
{
    public void badSink() throws Throwable
    {
        long data = CWE190_Integer_Overflow__long_rand_add_68a.data;
        long result = (long)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink() throws Throwable
    {
        long data = CWE190_Integer_Overflow__long_rand_add_68a.data;
        long result = (long)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink() throws Throwable
    {
        long data = CWE190_Integer_Overflow__long_rand_add_68a.data;
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
