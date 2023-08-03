
package testcases.CWE190_Integer_Overflow.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_random_add_71b
{
    public void badSink(Object dataObject ) throws Throwable
    {
        int data = (Integer)dataObject;
        int result = (int)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(Object dataObject ) throws Throwable
    {
        int data = (Integer)dataObject;
        int result = (int)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(Object dataObject ) throws Throwable
    {
        int data = (Integer)dataObject;
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
