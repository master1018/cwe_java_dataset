
package testcases.CWE400_Resource_Exhaustion.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE400_Resource_Exhaustion__max_value_for_loop_09 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int count;
        if (IO.STATIC_FINAL_TRUE)
        {
            count = Integer.MAX_VALUE;
        }
        else
        {
            count = 0;
        }
        if (IO.STATIC_FINAL_TRUE)
        {
            int i = 0;
            for (i = 0; i < count; i++)
            {
                IO.writeLine("Hello");
            }
        }
    }
    private void goodG2B1() throws Throwable
    {
        int count;
        if (IO.STATIC_FINAL_FALSE)
        {
            count = 0;
        }
        else
        {
            count = 2;
        }
        if (IO.STATIC_FINAL_TRUE)
        {
            int i = 0;
            for (i = 0; i < count; i++)
            {
                IO.writeLine("Hello");
            }
        }
    }
    private void goodG2B2() throws Throwable
    {
        int count;
        if (IO.STATIC_FINAL_TRUE)
        {
            count = 2;
        }
        else
        {
            count = 0;
        }
        if (IO.STATIC_FINAL_TRUE)
        {
            int i = 0;
            for (i = 0; i < count; i++)
            {
                IO.writeLine("Hello");
            }
        }
    }
    private void goodB2G1() throws Throwable
    {
        int count;
        if (IO.STATIC_FINAL_TRUE)
        {
            count = Integer.MAX_VALUE;
        }
        else
        {
            count = 0;
        }
        if (IO.STATIC_FINAL_FALSE)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            int i = 0;
            if (count > 0 && count <= 20)
            {
                for (i = 0; i < count; i++)
                {
                    IO.writeLine("Hello");
                }
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        int count;
        if (IO.STATIC_FINAL_TRUE)
        {
            count = Integer.MAX_VALUE;
        }
        else
        {
            count = 0;
        }
        if (IO.STATIC_FINAL_TRUE)
        {
            int i = 0;
            if (count > 0 && count <= 20)
            {
                for (i = 0; i < count; i++)
                {
                    IO.writeLine("Hello");
                }
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
