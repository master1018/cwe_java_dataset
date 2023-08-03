
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__long_console_readLine_sub_61a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        long data = (new CWE191_Integer_Underflow__long_console_readLine_sub_61b()).badSource();
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
        long data = (new CWE191_Integer_Underflow__long_console_readLine_sub_61b()).goodG2BSource();
        long result = (long)(data - 1);
        IO.writeLine("result: " + result);
    }
    private void goodB2G() throws Throwable
    {
        long data = (new CWE191_Integer_Underflow__long_console_readLine_sub_61b()).goodB2GSource();
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
