
package testcases.CWE398_Poor_Code_Quality;
import testcasesupport.*;
public class CWE398_Poor_Code_Quality__empty_while_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int i = 0;
        while(i++ < 10)
        {
        }
        IO.writeLine("Hello from bad()");
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
    {
        int i = 0;
        while(i++ < 10)
        {
            IO.writeLine("Inside the while statement");
        }
        IO.writeLine("Hello from good()");
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
