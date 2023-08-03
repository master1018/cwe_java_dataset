
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__Environment_printf_04 extends AbstractTestCase
{
    private static final boolean PRIVATE_STATIC_FINAL_TRUE = true;
    private static final boolean PRIVATE_STATIC_FINAL_FALSE = false;
    public void bad() throws Throwable
    {
        String data;
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            data = System.getenv("ADD");
        }
        else
        {
            data = null;
        }
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            if (data != null)
            {
                System.out.printf(data);
            }
        }
    }
    private void goodG2B1() throws Throwable
    {
        String data;
        if (PRIVATE_STATIC_FINAL_FALSE)
        {
            data = null;
        }
        else
        {
            data = "foo";
        }
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            if (data != null)
            {
                System.out.printf(data);
            }
        }
    }
    private void goodG2B2() throws Throwable
    {
        String data;
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            data = "foo";
        }
        else
        {
            data = null;
        }
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            if (data != null)
            {
                System.out.printf(data);
            }
        }
    }
    private void goodB2G1() throws Throwable
    {
        String data;
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            data = System.getenv("ADD");
        }
        else
        {
            data = null;
        }
        if (PRIVATE_STATIC_FINAL_FALSE)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if (data != null)
            {
                System.out.printf("%s%n", data);
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        String data;
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            data = System.getenv("ADD");
        }
        else
        {
            data = null;
        }
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            if (data != null)
            {
                System.out.printf("%s%n", data);
            }
        }
    }
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
        goodB2G1();
        goodB2G2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
