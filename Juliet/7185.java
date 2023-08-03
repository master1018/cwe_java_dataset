
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__Environment_printf_53d
{
    public void badSink(String data ) throws Throwable
    {
        if (data != null)
        {
            System.out.printf(data);
        }
    }
    public void goodG2BSink(String data ) throws Throwable
    {
        if (data != null)
        {
            System.out.printf(data);
        }
    }
    public void goodB2GSink(String data ) throws Throwable
    {
        if (data != null)
        {
            System.out.printf("%s%n", data);
        }
    }
}
