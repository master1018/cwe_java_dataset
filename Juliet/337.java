
package testcases.CWE197_Numeric_Truncation_Error.s01;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__int_console_readLine_to_short_53d
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
