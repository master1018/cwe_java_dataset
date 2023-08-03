
package testcases.CWE197_Numeric_Truncation_Error.s03;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__short_URLConnection_66b
{
    public void badSink(short dataArray[] ) throws Throwable
    {
        short data = dataArray[2];
        {
            IO.writeLine((byte)data);
        }
    }
    public void goodG2BSink(short dataArray[] ) throws Throwable
    {
        short data = dataArray[2];
        {
            IO.writeLine((byte)data);
        }
    }
}
