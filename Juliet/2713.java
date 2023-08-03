
package testcases.CWE400_Resource_Exhaustion.s03;
import testcasesupport.*;
public class CWE400_Resource_Exhaustion__sleep_max_value_02 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int count;
        if (true)
        {
            count = Integer.MAX_VALUE;
        }
        else
        {
            count = 0;
        }
        if (true)
        {
            Thread.sleep(count);
        }
    }
    private void goodG2B1() throws Throwable
    {
        int count;
        if (false)
        {
            count = 0;
        }
        else
        {
            count = 2;
        }
        if (true)
        {
            Thread.sleep(count);
        }
    }
    private void goodG2B2() throws Throwable
    {
        int count;
        if (true)
        {
            count = 2;
        }
        else
        {
            count = 0;
        }
        if (true)
        {
            Thread.sleep(count);
        }
    }
    private void goodB2G1() throws Throwable
    {
        int count;
        if (true)
        {
            count = Integer.MAX_VALUE;
        }
        else
        {
            count = 0;
        }
        if (false)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if (count > 0 && count <= 2000)
            {
                Thread.sleep(count);
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        int count;
        if (true)
        {
            count = Integer.MAX_VALUE;
        }
        else
        {
            count = 0;
        }
        if (true)
        {
            if (count > 0 && count <= 2000)
            {
                Thread.sleep(count);
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
