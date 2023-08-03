
package testcases.CWE398_Poor_Code_Quality;
import testcasesupport.*;
public class CWE398_Poor_Code_Quality__empty_while_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        while(true)
        {
            int i = 0;
            while(i++ < 10)
            {
            }
            IO.writeLine("Hello from bad()");
            break;
        }
    }
    private void good1() throws Throwable
    {
        while(true)
        {
            int i = 0;
            while(i++ < 10)
            {
                IO.writeLine("Inside the while statement");
            }
            IO.writeLine("Hello from good()");
            break;
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
