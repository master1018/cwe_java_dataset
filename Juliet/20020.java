
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__Environment_format_05 extends AbstractTestCase
{
    private boolean privateTrue = true;
    private boolean privateFalse = false;
    public void bad() throws Throwable
    {
        String data;
        if (privateTrue)
        {
            data = System.getenv("ADD");
        }
        else
        {
            data = null;
        }
        if (privateTrue)
        {
            if (data != null)
            {
                System.out.format(data);
            }
        }
    }
    private void goodG2B1() throws Throwable
    {
        String data;
        if (privateFalse)
        {
            data = null;
        }
        else
        {
            data = "foo";
        }
        if (privateTrue)
        {
            if (data != null)
            {
                System.out.format(data);
            }
        }
    }
    private void goodG2B2() throws Throwable
    {
        String data;
        if (privateTrue)
        {
            data = "foo";
        }
        else
        {
            data = null;
        }
        if (privateTrue)
        {
            if (data != null)
            {
                System.out.format(data);
            }
        }
    }
    private void goodB2G1() throws Throwable
    {
        String data;
        if (privateTrue)
        {
            data = System.getenv("ADD");
        }
        else
        {
            data = null;
        }
        if (privateFalse)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if (data != null)
            {
                System.out.format("%s%n", data);
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        String data;
        if (privateTrue)
        {
            data = System.getenv("ADD");
        }
        else
        {
            data = null;
        }
        if (privateTrue)
        {
            if (data != null)
            {
                System.out.format("%s%n", data);
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
