
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__long_min_sub_07 extends AbstractTestCase
{
    private int privateFive = 5;
    public void bad() throws Throwable
    {
        long data;
        if (privateFive==5)
        {
            data = Long.MIN_VALUE;
        }
        else
        {
            data = 0L;
        }
        if (privateFive==5)
        {
            long result = (long)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B1() throws Throwable
    {
        long data;
        if (privateFive!=5)
        {
            data = 0L;
        }
        else
        {
            data = 2;
        }
        if (privateFive==5)
        {
            long result = (long)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B2() throws Throwable
    {
        long data;
        if (privateFive==5)
        {
            data = 2;
        }
        else
        {
            data = 0L;
        }
        if (privateFive==5)
        {
            long result = (long)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodB2G1() throws Throwable
    {
        long data;
        if (privateFive==5)
        {
            data = Long.MIN_VALUE;
        }
        else
        {
            data = 0L;
        }
        if (privateFive!=5)
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
        if (privateFive==5)
        {
            data = Long.MIN_VALUE;
        }
        else
        {
            data = 0L;
        }
        if (privateFive==5)
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
