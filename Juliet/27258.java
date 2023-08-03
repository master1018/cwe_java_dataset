
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_init_variable_String_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = "Good";
        ; 
    }
    public void good() throws Throwable
    {
        goodB2G();
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
