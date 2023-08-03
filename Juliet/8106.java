
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_long_17 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        long data;
        data = 5L;
        for (int j = 0; j < 1; j++)
        {
            data = 10L;
            IO.writeLine("" + data);
        }
    }
    private void goodG2B() throws Throwable
    {
        long data;
        data = 5L;
        IO.writeLine("" + data);
        for (int j = 0; j < 1; j++)
        {
            data = 10L;
            IO.writeLine("" + data);
        }
    }
    private void goodB2G() throws Throwable
    {
        long data;
        data = 5L;
        for (int k = 0; k < 1; k++)
        {
            IO.writeLine("" + data);
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
