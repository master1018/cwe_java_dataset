
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_String_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = "Good";
        data = "Reinitialize";
        IO.writeLine(data);
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = "Good";
        IO.writeLine(data);
        data = "Reinitialize";
        IO.writeLine(data);
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = "Good";
        IO.writeLine(data);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
