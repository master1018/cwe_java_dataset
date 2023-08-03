
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__long_rand_multiply_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        long data;
        while (true)
        {
            data = (new java.security.SecureRandom()).nextLong();
            break;
        }
        while (true)
        {
            if(data < 0) 
            {
                long result = (long)(data * 2);
                IO.writeLine("result: " + result);
            }
            break;
        }
    }
    private void goodG2B() throws Throwable
    {
        long data;
        while (true)
        {
            data = 2;
            break;
        }
        while (true)
        {
            if(data < 0) 
            {
                long result = (long)(data * 2);
                IO.writeLine("result: " + result);
            }
            break;
        }
    }
    private void goodB2G() throws Throwable
    {
        long data;
        while (true)
        {
            data = (new java.security.SecureRandom()).nextLong();
            break;
        }
        while (true)
        {
            if(data < 0) 
            {
                if (data > (Long.MIN_VALUE/2))
                {
                    long result = (long)(data * 2);
                    IO.writeLine("result: " + result);
                }
                else
                {
                    IO.writeLine("data value is too small to perform multiplication.");
                }
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
