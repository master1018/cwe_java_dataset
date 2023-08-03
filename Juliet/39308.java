
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__database_format_61a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data = (new CWE134_Uncontrolled_Format_String__database_format_61b()).badSource();
        if (data != null)
        {
            System.out.format(data);
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        String data = (new CWE134_Uncontrolled_Format_String__database_format_61b()).goodG2BSource();
        if (data != null)
        {
            System.out.format(data);
        }
    }
    private void goodB2G() throws Throwable
    {
        String data = (new CWE134_Uncontrolled_Format_String__database_format_61b()).goodB2GSource();
        if (data != null)
        {
            System.out.format("%s%n", data);
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
