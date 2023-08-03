
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_uninit_variable_StringBuilder_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        StringBuilder data;
        ; 
        if (IO.staticReturnsTrueOrFalse())
        {
            ; 
        }
        else
        {
            data = new StringBuilder("Good");
            IO.writeLine(data.toString());
        }
    }
    private void goodB2G() throws Throwable
    {
        StringBuilder data;
        ; 
        if (IO.staticReturnsTrueOrFalse())
        {
            data = new StringBuilder("Good");
            IO.writeLine(data.toString());
        }
        else
        {
            data = new StringBuilder("Good");
            IO.writeLine(data.toString());
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
