
package testcases.CWE400_Resource_Exhaustion.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE400_Resource_Exhaustion__max_value_for_loop_05 extends AbstractTestCase
{
    private boolean privateTrue = true;
    private boolean privateFalse = false;
    public void bad() throws Throwable
    {
        int count;
        if (privateTrue)
        {
            count = Integer.MAX_VALUE;
        }
        else
        {
            count = 0;
        }
        if (privateTrue)
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
        if (privateFalse)
        {
            count = 0;
        }
        else
        {
            count = 2;
        }
        if (privateTrue)
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
        if (privateTrue)
        {
            count = 2;
        }
        else
        {
            count = 0;
        }
        if (privateTrue)
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
        if (privateTrue)
        {
            count = Integer.MAX_VALUE;
        }
        else
        {
            count = 0;
        }
        if (privateFalse)
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
        if (privateTrue)
        {
            count = Integer.MAX_VALUE;
        }
        else
        {
            count = 0;
        }
        if (privateTrue)
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
