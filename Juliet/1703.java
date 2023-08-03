
package testcases.CWE400_Resource_Exhaustion.s03;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE400_Resource_Exhaustion__sleep_random_08 extends AbstractTestCase
{
    private boolean privateReturnsTrue()
    {
        return true;
    }
    private boolean privateReturnsFalse()
    {
        return false;
    }
    public void bad() throws Throwable
    {
        int count;
        if (privateReturnsTrue())
        {
            count = (new SecureRandom()).nextInt();
        }
        else
        {
            count = 0;
        }
        if (privateReturnsTrue())
        {
            Thread.sleep(count);
        }
    }
    private void goodG2B1() throws Throwable
    {
        int count;
        if (privateReturnsFalse())
        {
            count = 0;
        }
        else
        {
            count = 2;
        }
        if (privateReturnsTrue())
        {
            Thread.sleep(count);
        }
    }
    private void goodG2B2() throws Throwable
    {
        int count;
        if (privateReturnsTrue())
        {
            count = 2;
        }
        else
        {
            count = 0;
        }
        if (privateReturnsTrue())
        {
            Thread.sleep(count);
        }
    }
    private void goodB2G1() throws Throwable
    {
        int count;
        if (privateReturnsTrue())
        {
            count = (new SecureRandom()).nextInt();
        }
        else
        {
            count = 0;
        }
        if (privateReturnsFalse())
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
        if (privateReturnsTrue())
        {
            count = (new SecureRandom()).nextInt();
        }
        else
        {
            count = 0;
        }
        if (privateReturnsTrue())
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
