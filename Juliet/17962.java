
package testcases.CWE191_Integer_Underflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE191_Integer_Underflow__int_Property_sub_67b
{
    public void badSink(CWE191_Integer_Underflow__int_Property_sub_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        int result = (int)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(CWE191_Integer_Underflow__int_Property_sub_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        int result = (int)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(CWE191_Integer_Underflow__int_Property_sub_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
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
