
package testcases.CWE190_Integer_Overflow.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_random_add_66b
{
    public void badSink(int dataArray[] ) throws Throwable
    {
        int data = dataArray[2];
        int result = (int)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(int dataArray[] ) throws Throwable
    {
        int data = dataArray[2];
        int result = (int)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(int dataArray[] ) throws Throwable
    {
        int data = dataArray[2];
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
