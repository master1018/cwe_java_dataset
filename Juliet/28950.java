
package testcases.CWE197_Numeric_Truncation_Error.s01;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__int_large_to_short_22b
{
    public int badSource() throws Throwable
    {
        int data;
        if (CWE197_Numeric_Truncation_Error__int_large_to_short_22a.badPublicStatic)
        {
            data = Short.MAX_VALUE + 5;
        }
        else
        {
            data = 0;
        }
        return data;
    }
    public int goodG2B1Source() throws Throwable
    {
        int data;
        if (CWE197_Numeric_Truncation_Error__int_large_to_short_22a.goodG2B1PublicStatic)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        return data;
    }
    public int goodG2B2Source() throws Throwable
    {
        int data;
        if (CWE197_Numeric_Truncation_Error__int_large_to_short_22a.goodG2B2PublicStatic)
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
