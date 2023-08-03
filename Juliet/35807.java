
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__Environment_format_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = System.getenv("ADD");
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
        String data;
        data = "foo";
        if (data != null)
        {
            System.out.format(data);
        }
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = System.getenv("ADD");
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
