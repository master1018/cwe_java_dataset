
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_init_variable_long_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        long data;
        data = 5L;
        if (IO.staticReturnsTrueOrFalse())
        {
            ; 
        }
        else
        {
            IO.writeLine("" + data);
        }
    }
    private void goodB2G() throws Throwable
    {
        long data;
        data = 5L;
        if (IO.staticReturnsTrueOrFalse())
        {
            IO.writeLine("" + data);
        }
        else
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
