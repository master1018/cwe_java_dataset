
package testcases.CWE400_Resource_Exhaustion.s03;
import testcasesupport.*;
public class CWE400_Resource_Exhaustion__sleep_max_value_03 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int count;
        if (5==5)
        {
            count = Integer.MAX_VALUE;
        }
        else
        {
            count = 0;
        }
        if (5==5)
        {
            Thread.sleep(count);
        }
    }
    private void goodG2B1() throws Throwable
    {
        int count;
        if (5!=5)
        {
            count = 0;
        }
        else
        {
            count = 2;
        }
        if (5==5)
        {
            Thread.sleep(count);
        }
    }
    private void goodG2B2() throws Throwable
    {
        int count;
        if (5==5)
        {
            count = 2;
        }
        else
        {
            count = 0;
        }
        if (5==5)
        {
            Thread.sleep(count);
        }
    }
    private void goodB2G1() throws Throwable
    {
        int count;
        if (5==5)
        {
            count = Integer.MAX_VALUE;
        }
        else
        {
            count = 0;
        }
        if (5!=5)
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
        if (5==5)
        {
            count = Integer.MAX_VALUE;
        }
        else
        {
            count = 0;
        }
        if (5==5)
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
