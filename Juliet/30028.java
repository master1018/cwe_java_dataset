
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_String_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        while (true)
        {
            data = "Good";
            break;
        }
        while (true)
        {
            data = "Reinitialize";
            IO.writeLine(data);
            break;
        }
    }
    private void goodG2B() throws Throwable
    {
        String data;
        while (true)
        {
            data = "Good";
            IO.writeLine(data);
            break;
        }
        while (true)
        {
            data = "Reinitialize";
            IO.writeLine(data);
            break;
        }
    }
    private void goodB2G() throws Throwable
    {
        String data;
        while (true)
        {
            data = "Good";
            break;
        }
        while (true)
        {
            IO.writeLine(data);
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
