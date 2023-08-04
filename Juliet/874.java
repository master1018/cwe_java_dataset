
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_StringBuilder_11 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        StringBuilder data;
        if (IO.staticReturnsTrue())
        {
            data = new StringBuilder("Good");
        }
        else
        {
            data = null;
        }
        if(IO.staticReturnsTrue())
        {
            data = new StringBuilder("Reinitialize");
            IO.writeLine(data.toString());
        }
    }
    private void goodG2B1() throws Throwable
    {
        StringBuilder data;
        if (IO.staticReturnsFalse())
        {
            data = null;
        }
        else
        {
            data = new StringBuilder("Good");
            IO.writeLine(data.toString());
        }
        if (IO.staticReturnsTrue())
        {
            data = new StringBuilder("Reinitialize");
            IO.writeLine(data.toString());
        }
    }
    private void goodG2B2() throws Throwable
    {
        StringBuilder data;
        if (IO.staticReturnsTrue())
        {
            data = new StringBuilder("Good");
            IO.writeLine(data.toString());
        }
        else
        {
            data = null;
        }
        if (IO.staticReturnsTrue())
        {
            data = new StringBuilder("Reinitialize");
            IO.writeLine(data.toString());
        }
    }
    private void goodB2G1() throws Throwable
    {
        StringBuilder data;
        if (IO.staticReturnsTrue())
        {
            data = new StringBuilder("Good");
        }
        else
        {
            data = null;
        }
        if (IO.staticReturnsFalse())
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            IO.writeLine(data.toString());
        }
    }
    private void goodB2G2() throws Throwable
    {
        StringBuilder data;
        if (IO.staticReturnsTrue())
        {
            data = new StringBuilder("Good");
        }
        else
        {
            data = null;
        }
        if (IO.staticReturnsTrue())
        {
            IO.writeLine(data.toString());
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