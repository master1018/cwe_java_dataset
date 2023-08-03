
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_init_variable_int_17 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = 5;
        for (int j = 0; j < 1; j++)
        {
            ; 
        }
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = 5;
        for (int k = 0; k < 1; k++)
        {
            IO.writeLine("" + data);
        }
    }
    public void good() throws Throwable
    {
        goodB2G();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
