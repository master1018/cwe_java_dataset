
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__int_array_04 extends AbstractTestCase
{
    private static final boolean PRIVATE_STATIC_FINAL_TRUE = true;
    private static final boolean PRIVATE_STATIC_FINAL_FALSE = false;
    public void bad() throws Throwable
    {
        int [] data;
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            data = null;
        }
        else
        {
            data = null;
        }
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            IO.writeLine("" + data.length);
        }
    }
    private void goodG2B1() throws Throwable
    {
        int [] data;
        if (PRIVATE_STATIC_FINAL_FALSE)
        {
            data = null;
        }
        else
        {
            data = new int[5];
        }
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            IO.writeLine("" + data.length);
        }
    }
    private void goodG2B2() throws Throwable
    {
        int [] data;
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            data = new int[5];
        }
        else
        {
            data = null;
        }
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            IO.writeLine("" + data.length);
        }
    }
    private void goodB2G1() throws Throwable
    {
        int [] data;
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            data = null;
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
                IO.writeLine("" + data.length);
            }
            else
            {
                IO.writeLine("data is null");
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        int [] data;
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            data = null;
        }
        else
        {
            data = null;
        }
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            if (data != null)
            {
                IO.writeLine("" + data.length);
            }
            else
            {
                IO.writeLine("data is null");
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
