
package testcases.CWE398_Poor_Code_Quality;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE398_Poor_Code_Quality__empty_case_17 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        for(int j = 0; j < 1; j++)
        {
            int x = (new SecureRandom()).nextInt();
            switch (x)
            {
            case 0:
                break;
            default:
                IO.writeLine("Inside the default statement");
                break;
            }
            IO.writeLine("Hello from bad()");
        }
    }
    private void good1() throws Throwable
    {
        for(int k = 0; k < 1; k++)
        {
            int x = (new SecureRandom()).nextInt();
            switch (x)
            {
            case 0:
                IO.writeLine("Inside the case statement");
                break;
            default:
                IO.writeLine("Inside the default statement");
                break;
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
