
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__byte_rand_square_22b
{
    public void badSink(byte data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__byte_rand_square_22a.badPublicStatic)
        {
            byte result = (byte)(data * data);
            IO.writeLine("result: " + result);
        }
        else
        {
            data = 0;
        }
    }
    public void goodB2G1Sink(byte data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__byte_rand_square_22a.goodB2G1PublicStatic)
        {
            data = 0;
        }
        else
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
    public void goodB2G2Sink(byte data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__byte_rand_square_22a.goodB2G2PublicStatic)
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
        else
        {
            data = 0;
        }
    }
    public void goodG2BSink(byte data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__byte_rand_square_22a.goodG2BPublicStatic)
        {
            byte result = (byte)(data * data);
            IO.writeLine("result: " + result);
        }
        else
        {
            data = 0;
        }
    }
}
