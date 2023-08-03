
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_String_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = "Good";
        }
        else
        {
            data = "Good";
            IO.writeLine(data);
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            data = "Reinitialize";
            IO.writeLine(data);
        }
        else
        {
            IO.writeLine(data);
        }
    }
    private void goodG2B() throws Throwable
    {
        String data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = "Good";
            IO.writeLine(data);
        }
        else
        {
            data = "Good";
            IO.writeLine(data);
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            data = "Reinitialize";
            IO.writeLine(data);
        }
        else
        {
            data = "Reinitialize";
            IO.writeLine(data);
        }
    }
    private void goodB2G() throws Throwable
    {
        String data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = "Good";
        }
        else
        {
            data = "Good";
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            IO.writeLine(data);
        }
        else
        {
            IO.writeLine(data);
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
