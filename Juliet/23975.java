
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_rand_multiply_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        long data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = (new java.security.SecureRandom()).nextLong();
        }
        else
        {
            data = 2;
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            if(data > 0) 
            {
                long result = (long)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
        else
        {
            if(data > 0) 
            {
                if (data < (Long.MAX_VALUE/2))
                {
                    long result = (long)(data * 2);
                    IO.writeLine("result: " + result);
                }
                else
                {
                    IO.writeLine("data value is too large to perform multiplication.");
                }
            }
        }
    }
    private void goodG2B() throws Throwable
    {
        long data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = 2;
        }
        else
        {
            data = 2;
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            if(data > 0) 
            {
                long result = (long)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
        else
        {
            if(data > 0) 
            {
                long result = (long)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
    }
    private void goodB2G() throws Throwable
    {
        long data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = (new java.security.SecureRandom()).nextLong();
        }
        else
        {
            data = (new java.security.SecureRandom()).nextLong();
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            if(data > 0) 
            {
                if (data < (Long.MAX_VALUE/2))
                {
                    long result = (long)(data * 2);
                    IO.writeLine("result: " + result);
                }
                else
                {
                    IO.writeLine("data value is too large to perform multiplication.");
                }
            }
        }
        else
        {
            if(data > 0) 
            {
                if (data < (Long.MAX_VALUE/2))
                {
                    long result = (long)(data * 2);
                    IO.writeLine("result: " + result);
                }
                else
                {
                    IO.writeLine("data value is too large to perform multiplication.");
                }
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
