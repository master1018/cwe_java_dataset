
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import java.util.Vector;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_console_readLine_add_72b
{
    public void badSink(Vector<Long> dataVector ) throws Throwable
    {
        long data = dataVector.remove(2);
        long result = (long)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(Vector<Long> dataVector ) throws Throwable
    {
        long data = dataVector.remove(2);
        long result = (long)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(Vector<Long> dataVector ) throws Throwable
    {
        long data = dataVector.remove(2);
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