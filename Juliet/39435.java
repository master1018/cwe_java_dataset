
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__long_min_sub_09 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        long data;
        if (IO.STATIC_FINAL_TRUE)
        {
            data = Long.MIN_VALUE;
        }
        else
        {
            data = 0L;
        }
        if (IO.STATIC_FINAL_TRUE)
        {
            long result = (long)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B1() throws Throwable
    {
        long data;
        if (IO.STATIC_FINAL_FALSE)
        {
            data = 0L;
        }
        else
        {
            data = 2;
        }
        if (IO.STATIC_FINAL_TRUE)
        {
            long result = (long)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B2() throws Throwable
    {
        long data;
        if (IO.STATIC_FINAL_TRUE)
        {
            data = 2;
        }
        else
        {
            data = 0L;
        }
        if (IO.STATIC_FINAL_TRUE)
        {
            long result = (long)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodB2G1() throws Throwable
    {
        long data;
        if (IO.STATIC_FINAL_TRUE)
        {
            data = Long.MIN_VALUE;
        }
        else
        {
            data = 0L;
        }
        if (IO.STATIC_FINAL_FALSE)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if (data > Long.MIN_VALUE)
            {
                long result = (long)(data - 1);
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
        long data;
        if (IO.STATIC_FINAL_TRUE)
        {
            data = Long.MIN_VALUE;
        }
        else
        {
            data = 0L;
        }
        if (IO.STATIC_FINAL_TRUE)
        {
            if (data > Long.MIN_VALUE)
            {
                long result = (long)(data - 1);
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
