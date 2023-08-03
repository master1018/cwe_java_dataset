
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__PropertiesFile_format_22b
{
    public void badSink(String data ) throws Throwable
    {
        if (CWE134_Uncontrolled_Format_String__PropertiesFile_format_22a.badPublicStatic)
        {
            if (data != null)
            {
                System.out.format(data);
            }
        }
        else
        {
            data = null;
        }
    }
    public void goodB2G1Sink(String data ) throws Throwable
    {
        if (CWE134_Uncontrolled_Format_String__PropertiesFile_format_22a.goodB2G1PublicStatic)
        {
            data = null;
        }
        else
        {
            if (data != null)
            {
                System.out.format("%s%n", data);
            }
        }
    }
    public void goodB2G2Sink(String data ) throws Throwable
    {
        if (CWE134_Uncontrolled_Format_String__PropertiesFile_format_22a.goodB2G2PublicStatic)
        {
            if (data != null)
            {
                System.out.format("%s%n", data);
            }
        }
        else
        {
            data = null;
        }
    }
    public void goodG2BSink(String data ) throws Throwable
    {
        if (CWE134_Uncontrolled_Format_String__PropertiesFile_format_22a.goodG2BPublicStatic)
        {
            if (data != null)
            {
                System.out.format(data);
            }
        }
        else
        {
            data = null;
        }
    }
}
