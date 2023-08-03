
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
public class CWE191_Integer_Underflow__byte_rand_multiply_09 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        byte data;
        if (IO.STATIC_FINAL_TRUE)
        {
            data = (byte)((new java.security.SecureRandom()).nextInt(1+Byte.MAX_VALUE-Byte.MIN_VALUE) + Byte.MIN_VALUE);
        }
        else
        {
            data = 0;
        }
        if (IO.STATIC_FINAL_TRUE)
        {
            if(data < 0) 
            {
                byte result = (byte)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
    }
    private void goodG2B1() throws Throwable
    {
        byte data;
        if (IO.STATIC_FINAL_FALSE)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        if (IO.STATIC_FINAL_TRUE)
        {
            if(data < 0) 
            {
                byte result = (byte)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
    }
    private void goodG2B2() throws Throwable
    {
        byte data;
        if (IO.STATIC_FINAL_TRUE)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        if (IO.STATIC_FINAL_TRUE)
        {
            if(data < 0) 
            {
                byte result = (byte)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
    }
    private void goodB2G1() throws Throwable
    {
        byte data;
        if (IO.STATIC_FINAL_TRUE)
        {
            data = (byte)((new java.security.SecureRandom()).nextInt(1+Byte.MAX_VALUE-Byte.MIN_VALUE) + Byte.MIN_VALUE);
        }
        else
        {
            data = 0;
        }
        if (IO.STATIC_FINAL_FALSE)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if(data < 0) 
            {
                if (data > (Byte.MIN_VALUE/2))
                {
                    byte result = (byte)(data * 2);
                    IO.writeLine("result: " + result);
                }
                else
                {
                    IO.writeLine("data value is too small to perform multiplication.");
                }
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        byte data;
        if (IO.STATIC_FINAL_TRUE)
        {
            data = (byte)((new java.security.SecureRandom()).nextInt(1+Byte.MAX_VALUE-Byte.MIN_VALUE) + Byte.MIN_VALUE);
        }
        else
        {
            data = 0;
        }
        if (IO.STATIC_FINAL_TRUE)
        {
            if(data < 0) 
            {
                if (data > (Byte.MIN_VALUE/2))
                {
                    byte result = (byte)(data * 2);
                    IO.writeLine("result: " + result);
                }
                else
                {
                    IO.writeLine("data value is too small to perform multiplication.");
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
