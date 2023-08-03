
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__connect_tcp_format_66b
{
    public void badSink(String dataArray[] ) throws Throwable
    {
        String data = dataArray[2];
        if (data != null)
        {
            System.out.format(data);
        }
    }
    public void goodG2BSink(String dataArray[] ) throws Throwable
    {
        String data = dataArray[2];
        if (data != null)
        {
            System.out.format(data);
        }
    }
    public void goodB2GSink(String dataArray[] ) throws Throwable
    {
        String data = dataArray[2];
        if (data != null)
        {
            System.out.format("%s%n", data);
        }
    }
}
