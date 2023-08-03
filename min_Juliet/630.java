
package testcases.CWE190_Integer_Overflow.s05;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_rand_add_22b
{
    public void badSink(short data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__short_rand_add_22a.badPublicStatic)
        {
            short result = (short)(data + 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            data = 0;
        }
    }
    public void goodB2G1Sink(short data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__short_rand_add_22a.goodB2G1PublicStatic)
        {
            data = 0;
        }
        else
        {
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
    public void goodB2G2Sink(short data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__short_rand_add_22a.goodB2G2PublicStatic)
        {
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
        else
        {
            data = 0;
        }
    }
    public void goodG2BSink(short data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__short_rand_add_22a.goodG2BPublicStatic)
        {
            short result = (short)(data + 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            data = 0;
        }
    }
}
