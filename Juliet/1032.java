
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_int_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = 5;
        }
        else
        {
            data = 5;
            IO.writeLine("" + data);
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            data = 10;
            IO.writeLine("" + data);
        }
        else
        {
            IO.writeLine("" + data);
        }
    }
    private void goodG2B() throws Throwable
    {
        int data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = 5;
            IO.writeLine("" + data);
        }
        else
        {
            data = 5;
            IO.writeLine("" + data);
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            data = 10;
            IO.writeLine("" + data);
        }
        else
        {
            data = 10;
            IO.writeLine("" + data);
        }
    }
    private void goodB2G() throws Throwable
    {
        int data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = 5;
        }
        else
        {
            data = 5;
        }
        if(IO.staticReturnsTrueOrFalse())
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
        goodG2B();
        goodB2G();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
