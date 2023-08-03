
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_long_05 extends AbstractTestCase
{
    private boolean privateTrue = true;
    private boolean privateFalse = false;
    public void bad() throws Throwable
    {
        long data;
        if (privateTrue)
        {
            data = 5L;
        }
        else
        {
            data = 0L;
        }
        if (privateTrue)
        {
            data = 10L;
            IO.writeLine("" + data);
        }
    }
    private void goodG2B1() throws Throwable
    {
        long data;
        if (privateFalse)
        {
            data = 0L;
        }
        else
        {
            data = 5L;
            IO.writeLine("" + data);
        }
        if (privateTrue)
        {
            data = 10L;
            IO.writeLine("" + data);
        }
    }
    private void goodG2B2() throws Throwable
    {
        long data;
        if (privateTrue)
        {
            data = 5L;
            IO.writeLine("" + data);
        }
        else
        {
            data = 0L;
        }
        if (privateTrue)
        {
            data = 10L;
            IO.writeLine("" + data);
        }
    }
    private void goodB2G1() throws Throwable
    {
        long data;
        if (privateTrue)
        {
            data = 5L;
        }
        else
        {
            data = 0L;
        }
        if (privateFalse)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            IO.writeLine("" + data);
        }
    }
    private void goodB2G2() throws Throwable
    {
        long data;
        if (privateTrue)
        {
            data = 5L;
        }
        else
        {
            data = 0L;
        }
        if (privateTrue)
        {
            IO.writeLine("" + data);
        }
    }
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
        goodB2G1();
        goodB2G2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
