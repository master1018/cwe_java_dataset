
package testcases.CWE197_Numeric_Truncation_Error.s01;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__int_File_to_short_68b
{
    public void badSink() throws Throwable
    {
        int data = CWE197_Numeric_Truncation_Error__int_File_to_short_68a.data;
        {
            IO.writeLine((short)data);
        }
    }
    public void goodG2BSink() throws Throwable
    {
        int data = CWE197_Numeric_Truncation_Error__int_File_to_short_68a.data;
        {
            IO.writeLine((short)data);
        }
    }
}
