
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__short_rand_multiply_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short data;
        while (true)
        {
            data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
            break;
        }
        while (true)
        {
            if(data < 0) 
            {
                short result = (short)(data * 2);
                IO.writeLine("result: " + result);
            }
            break;
        }
    }
    private void goodG2B() throws Throwable
    {
        short data;
        while (true)
        {
            data = 2;
            break;
        }
        while (true)
        {
            if(data < 0) 
            {
                short result = (short)(data * 2);
                IO.writeLine("result: " + result);
            }
            break;
        }
    }
    private void goodB2G() throws Throwable
    {
        short data;
        while (true)
        {
            data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
            break;
        }
        while (true)
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
