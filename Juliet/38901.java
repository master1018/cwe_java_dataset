
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_StringBuilder_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        StringBuilder data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = new StringBuilder("Good");
        }
        else
        {
            data = new StringBuilder("Good");
            IO.writeLine(data.toString());
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            data = new StringBuilder("Reinitialize");
            IO.writeLine(data.toString());
        }
        else
        {
            IO.writeLine(data.toString());
        }
    }
    private void goodG2B() throws Throwable
    {
        StringBuilder data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = new StringBuilder("Good");
            IO.writeLine(data.toString());
        }
        else
        {
            data = new StringBuilder("Good");
            IO.writeLine(data.toString());
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            data = new StringBuilder("Reinitialize");
            IO.writeLine(data.toString());
        }
        else
        {
            data = new StringBuilder("Reinitialize");
            IO.writeLine(data.toString());
        }
    }
    private void goodB2G() throws Throwable
    {
        StringBuilder data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = new StringBuilder("Good");
        }
        else
        {
            data = new StringBuilder("Good");
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            IO.writeLine(data.toString());
        }
        else
        {
            IO.writeLine(data.toString());
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
