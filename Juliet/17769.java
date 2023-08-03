
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE191_Integer_Underflow__int_URLConnection_sub_68b
{
    public void badSink() throws Throwable
    {
        int data = CWE191_Integer_Underflow__int_URLConnection_sub_68a.data;
        int result = (int)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink() throws Throwable
    {
        int data = CWE191_Integer_Underflow__int_URLConnection_sub_68a.data;
        int result = (int)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink() throws Throwable
    {
        int data = CWE191_Integer_Underflow__int_URLConnection_sub_68a.data;
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
