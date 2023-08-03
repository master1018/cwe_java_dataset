
package testcases.CWE398_Poor_Code_Quality;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE398_Poor_Code_Quality__empty_else_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int x;
        x = (new SecureRandom()).nextInt();
        if (x == 0)
        {
            IO.writeLine("Inside the else statement");
        }
        else
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
        int x;
        x = (new SecureRandom()).nextInt();
        if (x == 0)
        {
            IO.writeLine("Inside the if statement");
        }
        else
        {
            IO.writeLine("Inside the else statement");
        }
        IO.writeLine("Hello from good()");
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
