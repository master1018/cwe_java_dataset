
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_StringBuilder_15 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        StringBuilder data;
        switch (6)
        {
        case 6:
            data = new StringBuilder("Good");
            break;
        default:
            data = null;
            break;
        }
        switch (7)
        {
        case 7:
            data = new StringBuilder("Reinitialize");
            IO.writeLine(data.toString());
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodG2B1() throws Throwable
    {
        StringBuilder data;
        switch (5)
        {
        case 6:
            data = null;
            break;
        default:
            data = new StringBuilder("Good");
            IO.writeLine(data.toString());
            break;
        }
        switch (7)
        {
        case 7:
            data = new StringBuilder("Reinitialize");
            IO.writeLine(data.toString());
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodG2B2() throws Throwable
    {
        StringBuilder data;
        switch (6)
        {
        case 6:
            data = new StringBuilder("Good");
            IO.writeLine(data.toString());
            break;
        default:
            data = null;
            break;
        }
        switch (7)
        {
        case 7:
            data = new StringBuilder("Reinitialize");
            IO.writeLine(data.toString());
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodB2G1() throws Throwable
    {
        StringBuilder data;
        switch (6)
        {
        case 6:
            data = new StringBuilder("Good");
            break;
        default:
            data = null;
            break;
        }
        switch (8)
        {
        case 7:
            IO.writeLine("Benign, fixed string");
            break;
        default:
            IO.writeLine(data.toString());
            break;
        }
    }
    private void goodB2G2() throws Throwable
    {
        StringBuilder data;
        switch (6)
        {
        case 6:
            data = new StringBuilder("Good");
            break;
        default:
            data = null;
            break;
        }
        switch (7)
        {
        case 7:
            IO.writeLine(data.toString());
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
