
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE197_Numeric_Truncation_Error__short_random_22b
{
    public short badSource() throws Throwable
    {
        short data;
        if (CWE197_Numeric_Truncation_Error__short_random_22a.badPublicStatic)
        {
            data = (short)((new SecureRandom()).nextInt(Short.MAX_VALUE + 1));
        }
        else
        {
            data = 0;
        }
        return data;
    }
    public short goodG2B1Source() throws Throwable
    {
        short data;
        if (CWE197_Numeric_Truncation_Error__short_random_22a.goodG2B1PublicStatic)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        return data;
    }
    public short goodG2B2Source() throws Throwable
    {
        short data;
        if (CWE197_Numeric_Truncation_Error__short_random_22a.goodG2B2PublicStatic)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        return data;
    }
}
