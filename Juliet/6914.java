
package testcases.CWE190_Integer_Overflow.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_Property_square_52c
{
    public void badSink(int data ) throws Throwable
    {
        int result = (int)(data * data);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(int data ) throws Throwable
    {
        int result = (int)(data * data);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(int data ) throws Throwable
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
