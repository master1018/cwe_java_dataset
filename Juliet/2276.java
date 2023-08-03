
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE197_Numeric_Truncation_Error__short_random_31 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short dataCopy;
        {
            short data;
            data = (short)((new SecureRandom()).nextInt(Short.MAX_VALUE + 1));
            dataCopy = data;
        }
        {
            short data = dataCopy;
            {
                IO.writeLine((byte)data);
            }
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        short dataCopy;
        {
            short data;
            data = 2;
            dataCopy = data;
        }
        {
            short data = dataCopy;
            {
                IO.writeLine((byte)data);
            }
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
