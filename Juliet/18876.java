
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_rand_add_08 extends AbstractTestCase
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
        long data;
        if (privateReturnsTrue())
        {
            data = (new java.security.SecureRandom()).nextLong();
        }
        else
        {
            data = 0L;
        }
        if (privateReturnsTrue())
        {
            long result = (long)(data + 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B1() throws Throwable
    {
        long data;
        if (privateReturnsFalse())
        {
            data = 0L;
        }
        else
        {
            data = 2;
        }
        if (privateReturnsTrue())
        {
            long result = (long)(data + 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B2() throws Throwable
    {
        long data;
        if (privateReturnsTrue())
        {
            data = 2;
        }
        else
        {
            data = 0L;
        }
        if (privateReturnsTrue())
        {
            long result = (long)(data + 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodB2G1() throws Throwable
    {
        long data;
        if (privateReturnsTrue())
        {
            data = (new java.security.SecureRandom()).nextLong();
        }
        else
        {
            data = 0L;
        }
        if (privateReturnsFalse())
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if (data < Long.MAX_VALUE)
            {
                long result = (long)(data + 1);
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
        long data;
        if (privateReturnsTrue())
        {
            data = (new java.security.SecureRandom()).nextLong();
        }
        else
        {
            data = 0L;
        }
        if (privateReturnsTrue())
        {
            if (data < Long.MAX_VALUE)
            {
                long result = (long)(data + 1);
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
