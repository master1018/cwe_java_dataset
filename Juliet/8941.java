
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__Property_format_68a extends AbstractTestCase
{
    public static String data;
    public void bad() throws Throwable
    {
        data = System.getProperty("user.home");
        (new CWE134_Uncontrolled_Format_String__Property_format_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        data = "foo";
        (new CWE134_Uncontrolled_Format_String__Property_format_68b()).goodG2BSink();
    }
    private void goodB2G() throws Throwable
    {
        data = System.getProperty("user.home");
        (new CWE134_Uncontrolled_Format_String__Property_format_68b()).goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
