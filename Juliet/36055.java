
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__short_rand_multiply_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
        }
        else
        {
            data = 2;
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            if(data < 0) 
            {
                short result = (short)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
        else
        {
            if(data < 0) 
            {
                if (data > (Short.MIN_VALUE/2))
                {
                    short result = (short)(data * 2);
                    IO.writeLine("result: " + result);
                }
                else
                {
                    IO.writeLine("data value is too small to perform multiplication.");
                }
            }
        }
    }
    private void goodG2B() throws Throwable
    {
        short data;
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
            if(data < 0) 
            {
                short result = (short)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
        else
        {
            if(data < 0) 
            {
                short result = (short)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
    }
    private void goodB2G() throws Throwable
    {
        short data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
        }
        else
        {
            data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            if(data < 0) 
            {
                if (data > (Short.MIN_VALUE/2))
                {
                    short result = (short)(data * 2);
                    IO.writeLine("result: " + result);
                }
                else
                {
                    IO.writeLine("data value is too small to perform multiplication.");
                }
            }
        }
        else
        {
            if(data < 0) 
            {
                if (data > (Short.MIN_VALUE/2))
                {
                    short result = (short)(data * 2);
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
        goodG2B();
        goodB2G();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
