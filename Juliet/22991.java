
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__byte_max_add_68b
{
    public void badSink() throws Throwable
    {
        byte data = CWE190_Integer_Overflow__byte_max_add_68a.data;
        byte result = (byte)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink() throws Throwable
    {
        byte data = CWE190_Integer_Overflow__byte_max_add_68a.data;
        byte result = (byte)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink() throws Throwable
    {
        byte data = CWE190_Integer_Overflow__byte_max_add_68a.data;
        if (data < Byte.MAX_VALUE)
        {
            byte result = (byte)(data + 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too large to perform addition.");
        }
    }
}
