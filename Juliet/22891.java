
package testcases.CWE398_Poor_Code_Quality;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE398_Poor_Code_Quality__empty_case_01 extends AbstractTestCase
{
    public void bad() throws Throwable
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
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
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
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
