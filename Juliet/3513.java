
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__short_rand_multiply_07 extends AbstractTestCase
{
    private int privateFive = 5;
    public void bad() throws Throwable
    {
        short data;
        if (privateFive==5)
        {
            data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
        }
        else
        {
            data = 0;
        }
        if (privateFive==5)
        {
            if(data < 0) 
            {
                short result = (short)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
    }
    private void goodG2B1() throws Throwable
    {
        short data;
        if (privateFive!=5)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        if (privateFive==5)
        {
            if(data < 0) 
            {
                short result = (short)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
    }
    private void goodG2B2() throws Throwable
    {
        short data;
        if (privateFive==5)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        if (privateFive==5)
        {
            if(data < 0) 
            {
                short result = (short)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
    }
    private void goodB2G1() throws Throwable
    {
        short data;
        if (privateFive==5)
        {
            data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
        }
        else
        {
            data = 0;
        }
        if (privateFive!=5)
        {
            IO.writeLine("Benign, fixed string");
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
    private void goodB2G2() throws Throwable
    {
        short data;
        if (privateFive==5)
        {
            data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
        }
        else
        {
            data = 0;
        }
        if (privateFive==5)
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