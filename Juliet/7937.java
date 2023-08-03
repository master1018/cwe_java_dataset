
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__listen_tcp_format_52c
{
    public void badSink(String data ) throws Throwable
    {
        if (data != null)
        {
            System.out.format(data);
        }
    }
    public void goodG2BSink(String data ) throws Throwable
    {
        if (data != null)
        {
            System.out.format(data);
        }
    }
    public void goodB2GSink(String data ) throws Throwable
    {
        if (data != null)
        {
            System.out.format("%s%n", data);
        }
    }
}
