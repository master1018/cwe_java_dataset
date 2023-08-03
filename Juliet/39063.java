
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_uninit_variable_int_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        ; 
        while (true)
        {
            ; 
            break;
        }
    }
    private void goodB2G() throws Throwable
    {
        int data;
        ; 
        while (true)
        {
            data = 5;
            IO.writeLine("" + data);
            break;
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
