
package testcases.CWE197_Numeric_Truncation_Error.s01;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__int_large_to_short_67b
{
    public void badSink(CWE197_Numeric_Truncation_Error__int_large_to_short_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        {
            IO.writeLine((short)data);
        }
    }
    public void goodG2BSink(CWE197_Numeric_Truncation_Error__int_large_to_short_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        {
            IO.writeLine((short)data);
        }
    }
}
