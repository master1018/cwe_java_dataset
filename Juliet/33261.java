
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_String_11 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        if (IO.staticReturnsTrue())
        {
            data = "Good";
        }
        else
        {
            data = null;
        }
        if(IO.staticReturnsTrue())
        {
            data = "Reinitialize";
            IO.writeLine(data);
        }
    }
    private void goodG2B1() throws Throwable
    {
        String data;
        if (IO.staticReturnsFalse())
        {
            data = null;
        }
        else
        {
            data = "Good";
            IO.writeLine(data);
        }
        if (IO.staticReturnsTrue())
        {
            data = "Reinitialize";
            IO.writeLine(data);
        }
    }
    private void goodG2B2() throws Throwable
    {
        String data;
        if (IO.staticReturnsTrue())
        {
            data = "Good";
            IO.writeLine(data);
        }
        else
        {
            data = null;
        }
        if (IO.staticReturnsTrue())
        {
            data = "Reinitialize";
            IO.writeLine(data);
        }
    }
    private void goodB2G1() throws Throwable
    {
        String data;
        if (IO.staticReturnsTrue())
        {
            data = "Good";
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
            IO.writeLine(data);
        }
    }
    private void goodB2G2() throws Throwable
    {
        String data;
        if (IO.staticReturnsTrue())
        {
            data = "Good";
        }
        else
        {
            data = null;
        }
        if (IO.staticReturnsTrue())
        {
            IO.writeLine(data);
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
