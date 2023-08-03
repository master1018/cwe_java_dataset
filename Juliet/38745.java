
package testcases.CWE190_Integer_Overflow.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_max_add_22b
{
    public void badSink(int data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__int_max_add_22a.badPublicStatic)
        {
            int result = (int)(data + 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            data = 0;
        }
    }
    public void goodB2G1Sink(int data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__int_max_add_22a.goodB2G1PublicStatic)
        {
            data = 0;
        }
        else
        {
            if (data < Integer.MAX_VALUE)
            {
                int result = (int)(data + 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform addition.");
            }
        }
    }
    public void goodB2G2Sink(int data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__int_max_add_22a.goodB2G2PublicStatic)
        {
            if (data < Integer.MAX_VALUE)
            {
                int result = (int)(data + 1);
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
    public void goodG2BSink(int data ) throws Throwable
    {
        if (CWE190_Integer_Overflow__int_max_add_22a.goodG2BPublicStatic)
        {
            int result = (int)(data + 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            data = 0;
        }
    }
}
