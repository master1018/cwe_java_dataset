
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__byte_rand_square_54e
{
    public void badSink(byte data ) throws Throwable
    {
        byte result = (byte)(data * data);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(byte data ) throws Throwable
    {
        byte result = (byte)(data * data);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(byte data ) throws Throwable
    {
        if ((data != Integer.MIN_VALUE) && (data != Long.MIN_VALUE) && (Math.abs(data) <= (long)Math.sqrt(Byte.MAX_VALUE)))
        {
            byte result = (byte)(data * data);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too large to perform squaring.");
        }
    }
}
