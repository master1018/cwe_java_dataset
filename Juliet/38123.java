
package testcases.CWE400_Resource_Exhaustion.s03;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE400_Resource_Exhaustion__sleep_random_31 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int countCopy;
        {
            int count;
            count = (new SecureRandom()).nextInt();
            countCopy = count;
        }
        {
            int count = countCopy;
            Thread.sleep(count);
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int countCopy;
        {
            int count;
            count = 2;
            countCopy = count;
        }
        {
            int count = countCopy;
            Thread.sleep(count);
        }
    }
    private void goodB2G() throws Throwable
    {
        int countCopy;
        {
            int count;
            count = (new SecureRandom()).nextInt();
            countCopy = count;
        }
        {
            int count = countCopy;
            if (count > 0 && count <= 2000)
            {
                Thread.sleep(count);
            }
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
