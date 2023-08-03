
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__Property_format_41 extends AbstractTestCase
{
    private void badSink(String data ) throws Throwable
    {
        if (data != null)
        {
            System.out.format(data);
        }
    }
    public void bad() throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        badSink(data  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2BSink(String data ) throws Throwable
    {
        if (data != null)
        {
            System.out.format(data);
        }
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = "foo";
        goodG2BSink(data  );
    }
    private void goodB2GSink(String data ) throws Throwable
    {
        if (data != null)
        {
            System.out.format("%s%n", data);
        }
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        goodB2GSink(data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
