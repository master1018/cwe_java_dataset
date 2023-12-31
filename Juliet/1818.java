
package testcases.CWE190_Integer_Overflow.s05;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_max_square_11 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short data;
        if (IO.staticReturnsTrue())
        {
            data = Short.MAX_VALUE;
        }
        else
        {
            data = 0;
        }
        if(IO.staticReturnsTrue())
        {
            short result = (short)(data * data);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B1() throws Throwable
    {
        short data;
        if (IO.staticReturnsFalse())
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        if (IO.staticReturnsTrue())
        {
            short result = (short)(data * data);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B2() throws Throwable
    {
        short data;
        if (IO.staticReturnsTrue())
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        if (IO.staticReturnsTrue())
        {
            short result = (short)(data * data);
            IO.writeLine("result: " + result);
        }
    }
    private void goodB2G1() throws Throwable
    {
        short data;
        if (IO.staticReturnsTrue())
        {
            data = Short.MAX_VALUE;
        }
        else
        {
            data = 0;
        }
        if (IO.staticReturnsFalse())
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if ((data != Integer.MIN_VALUE) && (data != Long.MIN_VALUE) && (Math.abs(data) <= (long)Math.sqrt(Short.MAX_VALUE)))
            {
                short result = (short)(data * data);
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
        short data;
        if (IO.staticReturnsTrue())
        {
            data = Short.MAX_VALUE;
        }
        else
        {
            data = 0;
        }
        if (IO.staticReturnsTrue())
        {
            if ((data != Integer.MIN_VALUE) && (data != Long.MIN_VALUE) && (Math.abs(data) <= (long)Math.sqrt(Short.MAX_VALUE)))
            {
                short result = (short)(data * data);
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
