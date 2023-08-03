
package testcases.CWE134_Uncontrolled_Format_String.s02;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__URLConnection_printf_22b
{
    public void badSink(String data ) throws Throwable
    {
        if (CWE134_Uncontrolled_Format_String__URLConnection_printf_22a.badPublicStatic)
        {
            if (data != null)
            {
                System.out.printf(data);
            }
        }
        else
        {
            data = null;
        }
    }
    public void goodB2G1Sink(String data ) throws Throwable
    {
        if (CWE134_Uncontrolled_Format_String__URLConnection_printf_22a.goodB2G1PublicStatic)
        {
            data = null;
        }
        else
        {
            if (data != null)
            {
                System.out.printf("%s%n", data);
            }
        }
    }
    public void goodB2G2Sink(String data ) throws Throwable
    {
        if (CWE134_Uncontrolled_Format_String__URLConnection_printf_22a.goodB2G2PublicStatic)
        {
            if (data != null)
            {
                System.out.printf("%s%n", data);
            }
        }
        else
        {
            data = null;
        }
    }
    public void goodG2BSink(String data ) throws Throwable
    {
        if (CWE134_Uncontrolled_Format_String__URLConnection_printf_22a.goodG2BPublicStatic)
        {
            if (data != null)
            {
                System.out.printf(data);
            }
        }
        else
        {
            data = null;
        }
    }
}
