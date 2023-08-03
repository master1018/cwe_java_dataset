
package testcases.CWE197_Numeric_Truncation_Error.s03;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__short_URLConnection_71b
{
    public void badSink(Object dataObject ) throws Throwable
    {
        short data = (Short)dataObject;
        {
            IO.writeLine((byte)data);
        }
    }
    public void goodG2BSink(Object dataObject ) throws Throwable
    {
        short data = (Short)dataObject;
        {
            IO.writeLine((byte)data);
        }
    }
}
