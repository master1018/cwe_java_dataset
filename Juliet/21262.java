
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import java.util.Vector;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_max_add_72b
{
    public void badSink(Vector<Short> dataVector ) throws Throwable
    {
        short data = dataVector.remove(2);
        short result = (short)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(Vector<Short> dataVector ) throws Throwable
    {
        short data = dataVector.remove(2);
        short result = (short)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(Vector<Short> dataVector ) throws Throwable
    {
        short data = dataVector.remove(2);
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
