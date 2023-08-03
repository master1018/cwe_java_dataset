
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_StringBuilder_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        StringBuilder data;
        while (true)
        {
            data = new StringBuilder("Good");
            break;
        }
        while (true)
        {
            data = new StringBuilder("Reinitialize");
            IO.writeLine(data.toString());
            break;
        }
    }
    private void goodG2B() throws Throwable
    {
        StringBuilder data;
        while (true)
        {
            data = new StringBuilder("Good");
            IO.writeLine(data.toString());
            break;
        }
        while (true)
        {
            data = new StringBuilder("Reinitialize");
            IO.writeLine(data.toString());
            break;
        }
    }
    private void goodB2G() throws Throwable
    {
        StringBuilder data;
        while (true)
        {
            data = new StringBuilder("Good");
            break;
        }
        while (true)
        {
            IO.writeLine(data.toString());
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
