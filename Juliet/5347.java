
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__short_database_53d
{
    public void badSink(short data ) throws Throwable
    {
        {
            IO.writeLine((byte)data);
        }
    }
    public void goodG2BSink(short data ) throws Throwable
    {
        {
            IO.writeLine((byte)data);
        }
    }
}
