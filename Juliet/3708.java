
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE191_Integer_Underflow__int_database_sub_71b
{
    public void badSink(Object dataObject ) throws Throwable
    {
        int data = (Integer)dataObject;
        int result = (int)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(Object dataObject ) throws Throwable
    {
        int data = (Integer)dataObject;
        int result = (int)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(Object dataObject ) throws Throwable
    {
        int data = (Integer)dataObject;
        if (data > Integer.MIN_VALUE)
        {
            int result = (int)(data - 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too small to perform subtraction.");
        }
    }
}
