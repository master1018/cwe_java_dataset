
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__long_min_sub_42 extends AbstractTestCase
{
    private long badSource() throws Throwable
    {
        long data;
        data = Long.MIN_VALUE;
        return data;
    }
    public void bad() throws Throwable
    {
        long data = badSource();
        long result = (long)(data - 1);
        IO.writeLine("result: " + result);
    }
    private long goodG2BSource() throws Throwable
    {
        long data;
        data = 2;
        return data;
    }
    private void goodG2B() throws Throwable
    {
        long data = goodG2BSource();
        long result = (long)(data - 1);
        IO.writeLine("result: " + result);
    }
    private long goodB2GSource() throws Throwable
    {
        long data;
        data = Long.MIN_VALUE;
        return data;
    }
    private void goodB2G() throws Throwable
    {
        long data = goodB2GSource();
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
