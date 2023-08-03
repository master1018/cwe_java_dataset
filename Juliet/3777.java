
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE197_Numeric_Truncation_Error__int_random_to_short_61b
{
    public int badSource() throws Throwable
    {
        int data;
        data = (new SecureRandom()).nextInt();
        return data;
    }
    public int goodG2BSource() throws Throwable
    {
        int data;
        data = 2;
        return data;
    }
}
