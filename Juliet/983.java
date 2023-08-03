
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__short_console_readLine_sub_61a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short data = (new CWE191_Integer_Underflow__short_console_readLine_sub_61b()).badSource();
        short result = (short)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        short data = (new CWE191_Integer_Underflow__short_console_readLine_sub_61b()).goodG2BSource();
        short result = (short)(data - 1);
        IO.writeLine("result: " + result);
    }
    private void goodB2G() throws Throwable
    {
        short data = (new CWE191_Integer_Underflow__short_console_readLine_sub_61b()).goodB2GSource();
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
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
