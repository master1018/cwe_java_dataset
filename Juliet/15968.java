
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_long_07 extends AbstractTestCase
{
    private int privateFive = 5;
    public void bad() throws Throwable
    {
        long data;
        if (privateFive==5)
        {
            data = 5L;
        }
        else
        {
            data = 0L;
        }
        if (privateFive==5)
        {
            data = 10L;
            IO.writeLine("" + data);
        }
    }
    private void goodG2B1() throws Throwable
    {
        long data;
        if (privateFive!=5)
        {
            data = 0L;
        }
        else
        {
            data = 5L;
            IO.writeLine("" + data);
        }
        if (privateFive==5)
        {
            data = 10L;
            IO.writeLine("" + data);
        }
    }
    private void goodG2B2() throws Throwable
    {
        long data;
        if (privateFive==5)
        {
            data = 5L;
            IO.writeLine("" + data);
        }
        else
        {
            data = 0L;
        }
        if (privateFive==5)
        {
            data = 10L;
            IO.writeLine("" + data);
        }
    }
    private void goodB2G1() throws Throwable
    {
        long data;
        if (privateFive==5)
        {
            data = 5L;
        }
        else
        {
            data = 0L;
        }
        if (privateFive!=5)
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
        if (privateFive==5)
        {
            data = 5L;
        }
        else
        {
            data = 0L;
        }
        if (privateFive==5)
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
