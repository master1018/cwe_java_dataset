
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__long_min_sub_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        long data;
        while (true)
        {
            data = Long.MIN_VALUE;
            break;
        }
        while (true)
        {
            long result = (long)(data - 1);
            IO.writeLine("result: " + result);
            break;
        }
    }
    private void goodG2B() throws Throwable
    {
        long data;
        while (true)
        {
            data = 2;
            break;
        }
        while (true)
        {
            long result = (long)(data - 1);
            IO.writeLine("result: " + result);
            break;
        }
    }
    private void goodB2G() throws Throwable
    {
        long data;
        while (true)
        {
            data = Long.MIN_VALUE;
            break;
        }
        while (true)
        {
            if (data > Long.MIN_VALUE)
            {
                long result = (long)(data - 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform subtraction.");
            }
            break;
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
