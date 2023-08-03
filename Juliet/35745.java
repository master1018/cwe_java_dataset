
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__byte_console_readLine_add_54e
{
    public void badSink(byte data ) throws Throwable
    {
        byte result = (byte)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(byte data ) throws Throwable
    {
        byte result = (byte)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(byte data ) throws Throwable
    {
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
