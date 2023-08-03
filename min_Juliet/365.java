
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_StringBuilder_17 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        StringBuilder data;
        data = new StringBuilder("Good");
        for (int j = 0; j < 1; j++)
        {
            data = new StringBuilder("Reinitialize");
            IO.writeLine(data.toString());
        }
    }
    private void goodG2B() throws Throwable
    {
        StringBuilder data;
        data = new StringBuilder("Good");
        IO.writeLine(data.toString());
        for (int j = 0; j < 1; j++)
        {
            data = new StringBuilder("Reinitialize");
            IO.writeLine(data.toString());
        }
    }
    private void goodB2G() throws Throwable
    {
        StringBuilder data;
        data = new StringBuilder("Good");
        for (int k = 0; k < 1; k++)
        {
            IO.writeLine(data.toString());
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
