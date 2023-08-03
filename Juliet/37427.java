
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__short_File_68b
{
    public void badSink() throws Throwable
    {
        short data = CWE197_Numeric_Truncation_Error__short_File_68a.data;
        {
            IO.writeLine((byte)data);
        }
    }
    public void goodG2BSink() throws Throwable
    {
        short data = CWE197_Numeric_Truncation_Error__short_File_68a.data;
        {
            IO.writeLine((byte)data);
        }
    }
}
