
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__Environment_format_17 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        for (int j = 0; j < 1; j++)
        {
            if (data != null)
            {
                System.out.format(data);
            }
        }
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = "foo";
        for (int j = 0; j < 1; j++)
        {
            if (data != null)
            {
                System.out.format(data);
            }
        }
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        for (int k = 0; k < 1; k++)
        {
            if (data != null)
            {
                System.out.format("%s%n", data);
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
