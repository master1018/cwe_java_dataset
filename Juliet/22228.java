
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE197_Numeric_Truncation_Error__short_random_15 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short data = 0;
        switch (6)
        {
        case 6:
            data = (short)((new SecureRandom()).nextInt(Short.MAX_VALUE + 1));
            break;
        default:
            data = 0;
            break;
        }
        {
            IO.writeLine((byte)data);
        }
    }
    private void goodG2B1() throws Throwable
    {
        short data = 0;
        switch (5)
        {
        case 6:
            data = 0;
            break;
        default:
            data = 2;
            break;
        }
        {
            IO.writeLine((byte)data);
        }
    }
    private void goodG2B2() throws Throwable
    {
        short data = 0;
        switch (6)
        {
        case 6:
            data = 2;
            break;
        default:
            data = 0;
            break;
        }
        {
            IO.writeLine((byte)data);
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
