
package testcases.CWE398_Poor_Code_Quality;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE398_Poor_Code_Quality__empty_case_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (IO.staticReturnsTrueOrFalse())
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
        else
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
    private void good1() throws Throwable
    {
        if (IO.staticReturnsTrueOrFalse())
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
        else
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
