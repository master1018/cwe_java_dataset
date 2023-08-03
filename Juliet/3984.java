
package testcases.CWE400_Resource_Exhaustion.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE400_Resource_Exhaustion__random_for_loop_42 extends AbstractTestCase
{
    private int badSource() throws Throwable
    {
        int count;
        count = (new SecureRandom()).nextInt();
        return count;
    }
    public void bad() throws Throwable
    {
        int count = badSource();
        int i = 0;
        for (i = 0; i < count; i++)
        {
            IO.writeLine("Hello");
        }
    }
    private int goodG2BSource() throws Throwable
    {
        int count;
        count = 2;
        return count;
    }
    private void goodG2B() throws Throwable
    {
        int count = goodG2BSource();
        int i = 0;
        for (i = 0; i < count; i++)
        {
            IO.writeLine("Hello");
        }
    }
    private int goodB2GSource() throws Throwable
    {
        int count;
        count = (new SecureRandom()).nextInt();
        return count;
    }
    private void goodB2G() throws Throwable
    {
        int count = goodB2GSource();
        int i = 0;
        if (count > 0 && count <= 20)
        {
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
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
