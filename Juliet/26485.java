
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE197_Numeric_Truncation_Error__int_random_to_short_11 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        if (IO.staticReturnsTrue())
        {
            data = (new SecureRandom()).nextInt();
        }
        else
        {
            data = 0;
        }
        {
            IO.writeLine((short)data);
        }
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        if (IO.staticReturnsFalse())
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        {
            IO.writeLine((short)data);
        }
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        if (IO.staticReturnsTrue())
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        {
            IO.writeLine((short)data);
        }
    }
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
