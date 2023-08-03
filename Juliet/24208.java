
package testcases.CWE400_Resource_Exhaustion.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE400_Resource_Exhaustion__random_for_loop_31 extends AbstractTestCase
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
            int i = 0;
            for (i = 0; i < count; i++)
            {
                IO.writeLine("Hello");
            }
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
            int i = 0;
            for (i = 0; i < count; i++)
            {
                IO.writeLine("Hello");
            }
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
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
