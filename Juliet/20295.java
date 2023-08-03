
package testcases.CWE400_Resource_Exhaustion.s03;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE400_Resource_Exhaustion__sleep_random_41 extends AbstractTestCase
{
    private void badSink(int count ) throws Throwable
    {
        Thread.sleep(count);
    }
    public void bad() throws Throwable
    {
        int count;
        count = (new SecureRandom()).nextInt();
        badSink(count  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2BSink(int count ) throws Throwable
    {
        Thread.sleep(count);
    }
    private void goodG2B() throws Throwable
    {
        int count;
        count = 2;
        goodG2BSink(count  );
    }
    private void goodB2GSink(int count ) throws Throwable
    {
        if (count > 0 && count <= 2000)
        {
            Thread.sleep(count);
        }
    }
    private void goodB2G() throws Throwable
    {
        int count;
        count = (new SecureRandom()).nextInt();
        goodB2GSink(count  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
