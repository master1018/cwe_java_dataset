
package testcases.CWE398_Poor_Code_Quality;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE398_Poor_Code_Quality__empty_case_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        while(true)
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
            break;
        }
    }
    private void good1() throws Throwable
    {
        while(true)
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
