
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__int_URLConnection_to_short_52c
{
    public void badSink(int data ) throws Throwable
    {
        {
            IO.writeLine((short)data);
        }
    }
    public void goodG2BSink(int data ) throws Throwable
    {
        {
            IO.writeLine((short)data);
        }
    }
}
