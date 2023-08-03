
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__File_printf_81_bad extends CWE134_Uncontrolled_Format_String__File_printf_81_base
{
    public void action(String data ) throws Throwable
    {
        if (data != null)
        {
            System.out.printf(data);
        }
    }
}
