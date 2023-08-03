
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__Environment_format_51a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        (new CWE134_Uncontrolled_Format_String__Environment_format_51b()).badSink(data  );
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
        (new CWE134_Uncontrolled_Format_String__Environment_format_51b()).goodG2BSink(data  );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        (new CWE134_Uncontrolled_Format_String__Environment_format_51b()).goodB2GSink(data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
