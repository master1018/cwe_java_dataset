
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__Property_printf_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = System.getProperty("user.home");
        }
        else
        {
            data = "foo";
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            if (data != null)
            {
                System.out.printf(data);
            }
        }
        else
        {
            if (data != null)
            {
                System.out.printf("%s%n", data);
            }
        }
    }
    private void goodG2B() throws Throwable
    {
        String data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = "foo";
        }
        else
        {
            data = "foo";
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            if (data != null)
            {
                System.out.printf(data);
            }
        }
        else
        {
            if (data != null)
            {
                System.out.printf(data);
            }
        }
    }
    private void goodB2G() throws Throwable
    {
        String data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = System.getProperty("user.home");
        }
        else
        {
            data = System.getProperty("user.home");
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            if (data != null)
            {
                System.out.printf("%s%n", data);
            }
        }
        else
        {
            if (data != null)
            {
                System.out.printf("%s%n", data);
            }
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
