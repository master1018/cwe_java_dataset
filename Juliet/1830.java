
package testcases.CWE191_Integer_Underflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE191_Integer_Underflow__int_random_sub_08 extends AbstractTestCase
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
        int data;
        if (privateReturnsTrue())
        {
            data = (new SecureRandom()).nextInt();
        }
        else
        {
            data = 0;
        }
        if (privateReturnsTrue())
        {
            int result = (int)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        if (privateReturnsFalse())
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        if (privateReturnsTrue())
        {
            int result = (int)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        if (privateReturnsTrue())
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        if (privateReturnsTrue())
        {
            int result = (int)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodB2G1() throws Throwable
    {
        int data;
        if (privateReturnsTrue())
        {
            data = (new SecureRandom()).nextInt();
        }
        else
        {
            data = 0;
        }
        if (privateReturnsFalse())
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if (data > Integer.MIN_VALUE)
            {
                int result = (int)(data - 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform subtraction.");
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        int data;
        if (privateReturnsTrue())
        {
            data = (new SecureRandom()).nextInt();
        }
        else
        {
            data = 0;
        }
        if (privateReturnsTrue())
        {
            if (data > Integer.MIN_VALUE)
            {
                int result = (int)(data - 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform subtraction.");
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
