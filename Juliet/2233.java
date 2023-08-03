
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_long_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        long data;
        while (true)
        {
            data = 5L;
            break;
        }
        while (true)
        {
            data = 10L;
            IO.writeLine("" + data);
            break;
        }
    }
    private void goodG2B() throws Throwable
    {
        long data;
        while (true)
        {
            data = 5L;
            IO.writeLine("" + data);
            break;
        }
        while (true)
        {
            data = 10L;
            IO.writeLine("" + data);
            break;
        }
    }
    private void goodB2G() throws Throwable
    {
        long data;
        while (true)
        {
            data = 5L;
            break;
        }
        while (true)
        {
            IO.writeLine("" + data);
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
