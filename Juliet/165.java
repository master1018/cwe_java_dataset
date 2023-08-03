
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE197_Numeric_Truncation_Error__short_random_61b
{
    public short badSource() throws Throwable
    {
        short data;
        data = (short)((new SecureRandom()).nextInt(Short.MAX_VALUE + 1));
        return data;
    }
    public short goodG2BSource() throws Throwable
    {
        short data;
        data = 2;
        return data;
    }
}
