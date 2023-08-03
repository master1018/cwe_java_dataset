
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE197_Numeric_Truncation_Error__short_random_68a extends AbstractTestCase
{
    public static short data;
    public void bad() throws Throwable
    {
        data = (short)((new SecureRandom()).nextInt(Short.MAX_VALUE + 1));
        (new CWE197_Numeric_Truncation_Error__short_random_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        data = 2;
        (new CWE197_Numeric_Truncation_Error__short_random_68b()).goodG2BSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
