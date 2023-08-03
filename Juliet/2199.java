
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__Environment_printf_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        CWE134_Uncontrolled_Format_String__Environment_printf_81_base baseObject = new CWE134_Uncontrolled_Format_String__Environment_printf_81_bad();
        baseObject.action(data );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = "foo";
        CWE134_Uncontrolled_Format_String__Environment_printf_81_base baseObject = new CWE134_Uncontrolled_Format_String__Environment_printf_81_goodG2B();
        baseObject.action(data );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        CWE134_Uncontrolled_Format_String__Environment_printf_81_base baseObject = new CWE134_Uncontrolled_Format_String__Environment_printf_81_goodB2G();
        baseObject.action(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
