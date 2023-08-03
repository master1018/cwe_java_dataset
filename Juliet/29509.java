
package testcases.CWE400_Resource_Exhaustion.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE400_Resource_Exhaustion__random_for_loop_10 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int count;
        if (IO.staticTrue)
        {
            count = (new SecureRandom()).nextInt();
        }
        else
        {
            count = 0;
        }
        if (IO.staticTrue)
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
        if (IO.staticFalse)
        {
            count = 0;
        }
        else
        {
            count = 2;
        }
        if (IO.staticTrue)
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
        if (IO.staticTrue)
        {
            count = 2;
        }
        else
        {
            count = 0;
        }
        if (IO.staticTrue)
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
        if (IO.staticTrue)
        {
            count = (new SecureRandom()).nextInt();
        }
        else
        {
            count = 0;
        }
        if (IO.staticFalse)
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
        if (IO.staticTrue)
        {
            count = (new SecureRandom()).nextInt();
        }
        else
        {
            count = 0;
        }
        if (IO.staticTrue)
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
