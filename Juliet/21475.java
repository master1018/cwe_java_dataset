
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__short_min_multiply_10 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short data;
        if (IO.staticTrue)
        {
            data = Short.MIN_VALUE;
        }
        else
        {
            data = 0;
        }
        if (IO.staticTrue)
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
        if (IO.staticFalse)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        if (IO.staticTrue)
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
        if (IO.staticTrue)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        if (IO.staticTrue)
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
        if (IO.staticTrue)
        {
            data = Short.MIN_VALUE;
        }
        else
        {
            data = 0;
        }
        if (IO.staticFalse)
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
        if (IO.staticTrue)
        {
            data = Short.MIN_VALUE;
        }
        else
        {
            data = 0;
        }
        if (IO.staticTrue)
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
