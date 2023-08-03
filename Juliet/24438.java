
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__short_large_61b
{
    public short badSource() throws Throwable
    {
        short data;
        data = Byte.MAX_VALUE + 5;
        return data;
    }
    public short goodG2BSource() throws Throwable
    {
        short data;
        data = 2;
        return data;
    }
}
