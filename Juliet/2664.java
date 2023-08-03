
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_init_variable_int_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = 5;
        ; 
    }
    public void good() throws Throwable
    {
        goodB2G();
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = 5;
        IO.writeLine("" + data);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
