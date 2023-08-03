
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__Property_printf_81_goodG2B extends CWE134_Uncontrolled_Format_String__Property_printf_81_base
{
    public void action(String data ) throws Throwable
    {
        if (data != null)
        {
            System.out.printf(data);
        }
    }
}
