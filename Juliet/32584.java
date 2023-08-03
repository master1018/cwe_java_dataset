
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
public class CWE191_Integer_Underflow__byte_min_sub_14 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        byte data;
        if (IO.staticFive==5)
        {
            data = Byte.MIN_VALUE;
        }
        else
        {
            data = 0;
        }
        if (IO.staticFive==5)
        {
            byte result = (byte)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B1() throws Throwable
    {
        byte data;
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
            byte result = (byte)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B2() throws Throwable
    {
        byte data;
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
            byte result = (byte)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodB2G1() throws Throwable
    {
        byte data;
        if (IO.staticFive==5)
        {
            data = Byte.MIN_VALUE;
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
            if (data > Byte.MIN_VALUE)
            {
                byte result = (byte)(data - 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform subtraction.");
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        byte data;
        if (IO.staticFive==5)
        {
            data = Byte.MIN_VALUE;
        }
        else
        {
            data = 0;
        }
        if (IO.staticFive==5)
        {
            if (data > Byte.MIN_VALUE)
            {
                byte result = (byte)(data - 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform subtraction.");
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
