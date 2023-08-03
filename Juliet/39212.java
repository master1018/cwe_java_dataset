
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__PropertiesFile_printf_68b
{
    public void badSink() throws Throwable
    {
        String data = CWE134_Uncontrolled_Format_String__PropertiesFile_printf_68a.data;
        if (data != null)
        {
            System.out.printf(data);
        }
    }
    public void goodG2BSink() throws Throwable
    {
        String data = CWE134_Uncontrolled_Format_String__PropertiesFile_printf_68a.data;
        if (data != null)
        {
            System.out.printf(data);
        }
    }
    public void goodB2GSink() throws Throwable
    {
        String data = CWE134_Uncontrolled_Format_String__PropertiesFile_printf_68a.data;
        if (data != null)
        {
            System.out.printf("%s%n", data);
        }
    }
}
