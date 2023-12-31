
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__byte_max_square_08 extends AbstractTestCase
{
    private boolean privateReturnsTrue()
    {
        return true;
    }
    private boolean privateReturnsFalse()
    {
        return false;
    }
    public void bad() throws Throwable
    {
        byte data;
        if (privateReturnsTrue())
        {
            data = Byte.MAX_VALUE;
        }
        else
        {
            data = 0;
        }
        if (privateReturnsTrue())
        {
            byte result = (byte)(data * data);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B1() throws Throwable
    {
        byte data;
        if (privateReturnsFalse())
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        if (privateReturnsTrue())
        {
            byte result = (byte)(data * data);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B2() throws Throwable
    {
        byte data;
        if (privateReturnsTrue())
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        if (privateReturnsTrue())
        {
            byte result = (byte)(data * data);
            IO.writeLine("result: " + result);
        }
    }
    private void goodB2G1() throws Throwable
    {
        byte data;
        if (privateReturnsTrue())
        {
            data = Byte.MAX_VALUE;
        }
        else
        {
            data = 0;
        }
        if (privateReturnsFalse())
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if ((data != Integer.MIN_VALUE) && (data != Long.MIN_VALUE) && (Math.abs(data) <= (long)Math.sqrt(Byte.MAX_VALUE)))
            {
                byte result = (byte)(data * data);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform squaring.");
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        byte data;
        if (privateReturnsTrue())
        {
            data = Byte.MAX_VALUE;
        }
        else
        {
            data = 0;
        }
        if (privateReturnsTrue())
        {
            if ((data != Integer.MIN_VALUE) && (data != Long.MIN_VALUE) && (Math.abs(data) <= (long)Math.sqrt(Byte.MAX_VALUE)))
            {
                byte result = (byte)(data * data);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform squaring.");
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
