
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__short_min_sub_42 extends AbstractTestCase
{
    private short badSource() throws Throwable
    {
        short data;
        data = Short.MIN_VALUE;
        return data;
    }
    public void bad() throws Throwable
    {
        short data = badSource();
        short result = (short)(data - 1);
        IO.writeLine("result: " + result);
    }
    private short goodG2BSource() throws Throwable
    {
        short data;
        data = 2;
        return data;
    }
    private void goodG2B() throws Throwable
    {
        short data = goodG2BSource();
        short result = (short)(data - 1);
        IO.writeLine("result: " + result);
    }
    private short goodB2GSource() throws Throwable
    {
        short data;
        data = Short.MIN_VALUE;
        return data;
    }
    private void goodB2G() throws Throwable
    {
        short data = goodB2GSource();
        if (data > Short.MIN_VALUE)
        {
            short result = (short)(data - 1);
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
