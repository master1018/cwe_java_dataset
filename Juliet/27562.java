
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__Property_printf_61b
{
    public String badSource() throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        return data;
    }
    public String goodG2BSource() throws Throwable
    {
        String data;
        data = "foo";
        return data;
    }
    public String goodB2GSource() throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        return data;
    }
}
