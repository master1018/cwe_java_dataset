
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_uninit_variable_String_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        ; 
        if (IO.staticReturnsTrueOrFalse())
        {
            ; 
        }
        else
        {
            data = "Good";
            IO.writeLine(data);
        }
    }
    private void goodB2G() throws Throwable
    {
        String data;
        ; 
        if (IO.staticReturnsTrueOrFalse())
        {
            data = "Good";
            IO.writeLine(data);
        }
        else
        {
            data = "Good";
            IO.writeLine(data);
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
