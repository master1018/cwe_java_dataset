
package testcases.CWE400_Resource_Exhaustion.s03;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE400_Resource_Exhaustion__sleep_random_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int count;
        if(IO.staticReturnsTrueOrFalse())
        {
            count = (new SecureRandom()).nextInt();
        }
        else
        {
            count = 2;
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            Thread.sleep(count);
        }
        else
        {
            if (count > 0 && count <= 2000)
            {
                Thread.sleep(count);
            }
        }
    }
    private void goodG2B() throws Throwable
    {
        int count;
        if(IO.staticReturnsTrueOrFalse())
        {
            count = 2;
        }
        else
        {
            count = 2;
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            Thread.sleep(count);
        }
        else
        {
            Thread.sleep(count);
        }
    }
    private void goodB2G() throws Throwable
    {
        int count;
        if(IO.staticReturnsTrueOrFalse())
        {
            count = (new SecureRandom()).nextInt();
        }
        else
        {
            count = (new SecureRandom()).nextInt();
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            if (count > 0 && count <= 2000)
            {
                Thread.sleep(count);
            }
        }
        else
        {
            if (count > 0 && count <= 2000)
            {
                Thread.sleep(count);
            }
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
