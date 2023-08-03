
package testcases.CWE190_Integer_Overflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_database_square_68b
{
    public void badSink() throws Throwable
    {
        int data = CWE190_Integer_Overflow__int_database_square_68a.data;
        int result = (int)(data * data);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink() throws Throwable
    {
        int data = CWE190_Integer_Overflow__int_database_square_68a.data;
        int result = (int)(data * data);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink() throws Throwable
    {
        int data = CWE190_Integer_Overflow__int_database_square_68a.data;
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
