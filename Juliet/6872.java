
package testcases.CWE191_Integer_Underflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE191_Integer_Underflow__int_min_sub_11 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        if (IO.staticReturnsTrue())
        {
            data = Integer.MIN_VALUE;
        }
        else
        {
            data = 0;
        }
        if(IO.staticReturnsTrue())
        {
            int result = (int)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B1() throws Throwable
    {
        int data;
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
            int result = (int)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B2() throws Throwable
    {
        int data;
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
            int result = (int)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodB2G1() throws Throwable
    {
        int data;
        if (IO.staticReturnsTrue())
        {
            data = Integer.MIN_VALUE;
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
            if (data > Integer.MIN_VALUE)
            {
                int result = (int)(data - 1);
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
        int data;
        if (IO.staticReturnsTrue())
        {
            data = Integer.MIN_VALUE;
        }
        else
        {
            data = 0;
        }
        if (IO.staticReturnsTrue())
        {
            if (data > Integer.MIN_VALUE)
            {
                int result = (int)(data - 1);
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
