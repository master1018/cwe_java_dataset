
package testcases.CWE134_Uncontrolled_Format_String.s02;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__URLConnection_format_53d
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
