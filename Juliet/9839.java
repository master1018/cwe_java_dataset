
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_URLConnection_square_22b
{
    public void badSink(int data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__int_URLConnection_square_22a.badPublicStatic)
        {
            int result = (int)(data * data);
            IO.writeLine("result: " + result);
        }
        else
        {
            data = 0;
        }
    }
    public void goodB2G1Sink(int data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__int_URLConnection_square_22a.goodB2G1PublicStatic)
        {
            data = 0;
        }
        else
        {
            if ((data != Integer.MIN_VALUE) && (data != Long.MIN_VALUE) && (Math.abs(data) <= (long)Math.sqrt(Integer.MAX_VALUE)))
            {
                int result = (int)(data * data);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform squaring.");
            }
        }
    }
    public void goodB2G2Sink(int data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__int_URLConnection_square_22a.goodB2G2PublicStatic)
        {
            if ((data != Integer.MIN_VALUE) && (data != Long.MIN_VALUE) && (Math.abs(data) <= (long)Math.sqrt(Integer.MAX_VALUE)))
            {
                int result = (int)(data * data);
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
    public void goodG2BSink(int data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__int_URLConnection_square_22a.goodG2BPublicStatic)
        {
            int result = (int)(data * data);
            IO.writeLine("result: " + result);
        }
        else
        {
            data = 0;
        }
    }
}
