
package testcases.CWE398_Poor_Code_Quality;
import testcasesupport.*;
public class CWE398_Poor_Code_Quality__empty_for_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (IO.staticReturnsTrueOrFalse())
        {
            for (int i = 0; i < 10; i++)
            {
            }
            IO.writeLine("Hello from bad()");
        }
        else
        {
            for (int i = 0; i < 10; i++)
            {
                IO.writeLine("Inside the for statement");
            }
            IO.writeLine("Hello from good()");
        }
    }
    private void good1() throws Throwable
    {
        if (IO.staticReturnsTrueOrFalse())
        {
            for (int i = 0; i < 10; i++)
            {
                IO.writeLine("Inside the for statement");
            }
            IO.writeLine("Hello from good()");
        }
        else
        {
            for (int i = 0; i < 10; i++)
            {
                IO.writeLine("Inside the for statement");
            }
            IO.writeLine("Hello from good()");
        }
    }
    public void good() throws Throwable
    {
        good1();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
