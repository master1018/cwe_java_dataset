
package testcases.CWE197_Numeric_Truncation_Error.s01;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__int_connect_tcp_to_byte_68b
{
    public void badSink() throws Throwable
    {
        int data = CWE197_Numeric_Truncation_Error__int_connect_tcp_to_byte_68a.data;
        {
            IO.writeLine((byte)data);
        }
    }
    public void goodG2BSink() throws Throwable
    {
        int data = CWE197_Numeric_Truncation_Error__int_connect_tcp_to_byte_68a.data;
        {
            IO.writeLine((byte)data);
        }
    }
}