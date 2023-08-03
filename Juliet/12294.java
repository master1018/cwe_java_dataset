
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_String_15 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        switch (6)
        {
        case 6:
            data = "Good";
            break;
        default:
            data = null;
            break;
        }
        switch (7)
        {
        case 7:
            data = "Reinitialize";
            IO.writeLine(data);
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodG2B1() throws Throwable
    {
        String data;
        switch (5)
        {
        case 6:
            data = null;
            break;
        default:
            data = "Good";
            IO.writeLine(data);
            break;
        }
        switch (7)
        {
        case 7:
            data = "Reinitialize";
            IO.writeLine(data);
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodG2B2() throws Throwable
    {
        String data;
        switch (6)
        {
        case 6:
            data = "Good";
            IO.writeLine(data);
            break;
        default:
            data = null;
            break;
        }
        switch (7)
        {
        case 7:
            data = "Reinitialize";
            IO.writeLine(data);
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodB2G1() throws Throwable
    {
        String data;
        switch (6)
        {
        case 6:
            data = "Good";
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
            IO.writeLine(data);
            break;
        }
    }
    private void goodB2G2() throws Throwable
    {
        String data;
        switch (6)
        {
        case 6:
            data = "Good";
            break;
        default:
            data = null;
            break;
        }
        switch (7)
        {
        case 7:
            IO.writeLine(data);
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
