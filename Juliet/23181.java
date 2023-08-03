
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_long_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        long data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = 5L;
        }
        else
        {
            data = 5L;
            IO.writeLine("" + data);
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            data = 10L;
            IO.writeLine("" + data);
        }
        else
        {
            IO.writeLine("" + data);
        }
    }
    private void goodG2B() throws Throwable
    {
        long data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = 5L;
            IO.writeLine("" + data);
        }
        else
        {
            data = 5L;
            IO.writeLine("" + data);
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            data = 10L;
            IO.writeLine("" + data);
        }
        else
        {
            data = 10L;
            IO.writeLine("" + data);
        }
    }
    private void goodB2G() throws Throwable
    {
        long data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = 5L;
        }
        else
        {
            data = 5L;
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
