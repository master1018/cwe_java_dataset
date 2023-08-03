
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_int_02 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        if (true)
        {
            data = 5;
        }
        else
        {
            data = 0;
        }
        if (true)
        {
            data = 10;
            IO.writeLine("" + data);
        }
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        if (false)
        {
            data = 0;
        }
        else
        {
            data = 5;
            IO.writeLine("" + data);
        }
        if (true)
        {
            data = 10;
            IO.writeLine("" + data);
        }
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        if (true)
        {
            data = 5;
            IO.writeLine("" + data);
        }
        else
        {
            data = 0;
        }
        if (true)
        {
            data = 10;
            IO.writeLine("" + data);
        }
    }
    private void goodB2G1() throws Throwable
    {
        int data;
        if (true)
        {
            data = 5;
        }
        else
        {
            data = 0;
        }
        if (false)
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
        int data;
        if (true)
        {
            data = 5;
        }
        else
        {
            data = 0;
        }
        if (true)
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
