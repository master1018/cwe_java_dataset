
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
public class CWE191_Integer_Underflow__byte_min_multiply_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        byte data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = Byte.MIN_VALUE;
        }
        else
        {
            data = 2;
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            if(data < 0) 
            {
                byte result = (byte)(data * 2);
                IO.writeLine("result: " + result);
            }
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
    private void goodG2B() throws Throwable
    {
        byte data;
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
                byte result = (byte)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
        else
        {
            if(data < 0) 
            {
                byte result = (byte)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
    }
    private void goodB2G() throws Throwable
    {
        byte data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = Byte.MIN_VALUE;
        }
        else
        {
            data = Byte.MIN_VALUE;
        }
        if(IO.staticReturnsTrueOrFalse())
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
