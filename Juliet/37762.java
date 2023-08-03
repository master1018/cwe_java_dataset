
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__int_URLConnection_to_short_66b
{
    public void badSink(int dataArray[] ) throws Throwable
    {
        int data = dataArray[2];
        {
            IO.writeLine((short)data);
        }
    }
    public void goodG2BSink(int dataArray[] ) throws Throwable
    {
        int data = dataArray[2];
        {
            IO.writeLine((short)data);
        }
    }
}
