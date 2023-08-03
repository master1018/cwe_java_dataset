
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__Property_format_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        while (true)
        {
            data = System.getProperty("user.home");
            break;
        }
        while (true)
        {
            if (data != null)
            {
                System.out.format(data);
            }
            break;
        }
    }
    private void goodG2B() throws Throwable
    {
        String data;
        while (true)
        {
            data = "foo";
            break;
        }
        while (true)
        {
            if (data != null)
            {
                System.out.format(data);
            }
            break;
        }
    }
    private void goodB2G() throws Throwable
    {
        String data;
        while (true)
        {
            data = System.getProperty("user.home");
            break;
        }
        while (true)
        {
            if (data != null)
            {
                System.out.format("%s%n", data);
            }
            break;
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
