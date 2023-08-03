
package testcases.CWE197_Numeric_Truncation_Error.s01;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__int_listen_tcp_to_byte_53d
{
    public void badSink(int data ) throws Throwable
    {
        {
            IO.writeLine((byte)data);
        }
    }
    public void goodG2BSink(int data ) throws Throwable
    {
        {
            IO.writeLine((byte)data);
        }
    }
}
