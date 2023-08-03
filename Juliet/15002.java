
package testcases.CWE398_Poor_Code_Quality;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE398_Poor_Code_Quality__empty_else_17 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        for(int j = 0; j < 1; j++)
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
    }
    private void good1() throws Throwable
    {
        for(int k = 0; k < 1; k++)
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
