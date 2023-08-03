
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__int_URLConnection_to_byte_67b
{
    public void badSink(CWE197_Numeric_Truncation_Error__int_URLConnection_to_byte_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        {
            IO.writeLine((byte)data);
        }
    }
    public void goodG2BSink(CWE197_Numeric_Truncation_Error__int_URLConnection_to_byte_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        {
            IO.writeLine((byte)data);
        }
    }
}
