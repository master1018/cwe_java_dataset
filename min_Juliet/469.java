
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_int_15 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        switch (6)
        {
        case 6:
            data = 5;
            break;
        default:
            data = 0;
            break;
        }
        switch (7)
        {
        case 7:
            data = 10;
            IO.writeLine("" + data);
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        switch (5)
        {
        case 6:
            data = 0;
            break;
        default:
            data = 5;
            IO.writeLine("" + data);
            break;
        }
        switch (7)
        {
        case 7:
            data = 10;
            IO.writeLine("" + data);
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        switch (6)
        {
        case 6:
            data = 5;
            IO.writeLine("" + data);
            break;
        default:
            data = 0;
            break;
        }
        switch (7)
        {
        case 7:
            data = 10;
            IO.writeLine("" + data);
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodB2G1() throws Throwable
    {
        int data;
        switch (6)
        {
        case 6:
            data = 5;
            break;
        default:
            data = 0;
            break;
        }
        switch (8)
        {
        case 7:
            IO.writeLine("Benign, fixed string");
            break;
        default:
            IO.writeLine("" + data);
            break;
        }
    }
    private void goodB2G2() throws Throwable
    {
        int data;
        switch (6)
        {
        case 6:
            data = 5;
            break;
        default:
            data = 0;
            break;
        }
        switch (7)
        {
        case 7:
            IO.writeLine("" + data);
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
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
