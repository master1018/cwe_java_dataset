
package testcases.CWE190_Integer_Overflow.s05;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_rand_multiply_04 extends AbstractTestCase
{
    private static final boolean PRIVATE_STATIC_FINAL_TRUE = true;
    private static final boolean PRIVATE_STATIC_FINAL_FALSE = false;
    public void bad() throws Throwable
    {
        short data;
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
        }
        else
        {
            data = 0;
        }
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            if(data > 0) 
            {
                short result = (short)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
    }
    private void goodG2B1() throws Throwable
    {
        short data;
        if (PRIVATE_STATIC_FINAL_FALSE)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            if(data > 0) 
            {
                short result = (short)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
    }
    private void goodG2B2() throws Throwable
    {
        short data;
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            if(data > 0) 
            {
                short result = (short)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
    }
    private void goodB2G1() throws Throwable
    {
        short data;
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
        }
        else
        {
            data = 0;
        }
        if (PRIVATE_STATIC_FINAL_FALSE)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if(data > 0) 
            {
                if (data < (Short.MAX_VALUE/2))
                {
                    short result = (short)(data * 2);
                    IO.writeLine("result: " + result);
                }
                else
                {
                    IO.writeLine("data value is too large to perform multiplication.");
                }
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        short data;
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
        }
        else
        {
            data = 0;
        }
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            if(data > 0) 
            {
                if (data < (Short.MAX_VALUE/2))
                {
                    short result = (short)(data * 2);
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
