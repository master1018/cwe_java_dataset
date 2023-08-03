
package testcases.CWE400_Resource_Exhaustion.s03;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE400_Resource_Exhaustion__sleep_random_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int count;
        while (true)
        {
            count = (new SecureRandom()).nextInt();
            break;
        }
        while (true)
        {
            Thread.sleep(count);
            break;
        }
    }
    private void goodG2B() throws Throwable
    {
        int count;
        while (true)
        {
            count = 2;
            break;
        }
        while (true)
        {
            Thread.sleep(count);
            break;
        }
    }
    private void goodB2G() throws Throwable
    {
        int count;
        while (true)
        {
            count = (new SecureRandom()).nextInt();
            break;
        }
        while (true)
        {
            if (count > 0 && count <= 2000)
            {
                Thread.sleep(count);
            }
            break;
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
