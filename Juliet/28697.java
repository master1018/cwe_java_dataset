
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__PropertiesFile_format_67b
{
    public void badSink(CWE134_Uncontrolled_Format_String__PropertiesFile_format_67a.Container dataContainer ) throws Throwable
    {
        String data = dataContainer.containerOne;
        if (data != null)
        {
            System.out.format(data);
        }
    }
    public void goodG2BSink(CWE134_Uncontrolled_Format_String__PropertiesFile_format_67a.Container dataContainer ) throws Throwable
    {
        String data = dataContainer.containerOne;
        if (data != null)
        {
            System.out.format(data);
        }
    }
    public void goodB2GSink(CWE134_Uncontrolled_Format_String__PropertiesFile_format_67a.Container dataContainer ) throws Throwable
    {
        String data = dataContainer.containerOne;
        if (data != null)
        {
            System.out.format("%s%n", data);
        }
    }
}
