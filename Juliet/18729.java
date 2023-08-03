
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__long_min_sub_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        long data;
        data = Long.MIN_VALUE;
        long result = (long)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        long data;
        data = 2;
        long result = (long)(data - 1);
        IO.writeLine("result: " + result);
    }
    private void goodB2G() throws Throwable
    {
        long data;
        data = Long.MIN_VALUE;
        if (data > Long.MIN_VALUE)
        {
            long result = (long)(data - 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too small to perform subtraction.");
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
