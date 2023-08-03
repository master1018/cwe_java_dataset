
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__Property_format_42 extends AbstractTestCase
{
    private String badSource() throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        return data;
    }
    public void bad() throws Throwable
    {
        String data = badSource();
        if (data != null)
        {
            System.out.format(data);
        }
    }
    private String goodG2BSource() throws Throwable
    {
        String data;
        data = "foo";
        return data;
    }
    private void goodG2B() throws Throwable
    {
        String data = goodG2BSource();
        if (data != null)
        {
            System.out.format(data);
        }
    }
    private String goodB2GSource() throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        return data;
    }
    private void goodB2G() throws Throwable
    {
        String data = goodB2GSource();
        if (data != null)
        {
            System.out.format("%s%n", data);
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
