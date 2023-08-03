
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__byte_max_add_22b
{
    public void badSink(byte data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__byte_max_add_22a.badPublicStatic)
        {
            byte result = (byte)(data + 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            data = 0;
        }
    }
    public void goodB2G1Sink(byte data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__byte_max_add_22a.goodB2G1PublicStatic)
        {
            data = 0;
        }
        else
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
    public void goodB2G2Sink(byte data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__byte_max_add_22a.goodB2G2PublicStatic)
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
        else
        {
            data = 0;
        }
    }
    public void goodG2BSink(byte data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__byte_max_add_22a.goodG2BPublicStatic)
        {
            byte result = (byte)(data + 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            data = 0;
        }
    }
}
