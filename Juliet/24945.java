
package testcases.CWE190_Integer_Overflow.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE190_Integer_Overflow__int_random_add_02 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        if (true)
        {
            data = (new SecureRandom()).nextInt();
        }
        else
        {
            data = 0;
        }
        if (true)
        {
            int result = (int)(data + 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        if (false)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        if (true)
        {
            int result = (int)(data + 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        if (true)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        if (true)
        {
            int result = (int)(data + 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodB2G1() throws Throwable
    {
        int data;
        if (true)
        {
            data = (new SecureRandom()).nextInt();
        }
        else
        {
            data = 0;
        }
        if (false)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if (data < Integer.MAX_VALUE)
            {
                int result = (int)(data + 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform addition.");
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        int data;
        if (true)
        {
            data = (new SecureRandom()).nextInt();
        }
        else
        {
            data = 0;
        }
        if (true)
        {
            if (data < Integer.MAX_VALUE)
            {
                int result = (int)(data + 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform addition.");
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
