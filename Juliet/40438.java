
package testcases.CWE190_Integer_Overflow.s05;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_max_multiply_14 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short data;
        if (IO.staticFive==5)
        {
            data = Short.MAX_VALUE;
        }
        else
        {
            data = 0;
        }
        if (IO.staticFive==5)
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
        if (IO.staticFive!=5)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        if (IO.staticFive==5)
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
        if (IO.staticFive==5)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        if (IO.staticFive==5)
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
        if (IO.staticFive==5)
        {
            data = Short.MAX_VALUE;
        }
        else
        {
            data = 0;
        }
        if (IO.staticFive!=5)
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
        if (IO.staticFive==5)
        {
            data = Short.MAX_VALUE;
        }
        else
        {
            data = 0;
        }
        if (IO.staticFive==5)
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
