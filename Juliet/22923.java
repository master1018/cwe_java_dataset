
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_uninit_variable_long_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        long data;
        ; 
        if (IO.staticReturnsTrueOrFalse())
        {
            ; 
        }
        else
        {
            data = 5L;
            IO.writeLine("" + data);
        }
    }
    private void goodB2G() throws Throwable
    {
        long data;
        ; 
        if (IO.staticReturnsTrueOrFalse())
        {
            data = 5L;
            IO.writeLine("" + data);
        }
        else
        {
            data = 5L;
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
