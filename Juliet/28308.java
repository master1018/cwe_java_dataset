
package testcases.CWE134_Uncontrolled_Format_String.s02;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__URLConnection_printf_81_bad extends CWE134_Uncontrolled_Format_String__URLConnection_printf_81_base
{
    public void action(String data ) throws Throwable
    {
        if (data != null)
        {
            System.out.printf(data);
        }
    }
}
