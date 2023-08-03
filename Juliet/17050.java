
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__long_min_sub_21 extends AbstractTestCase
{
    private boolean badPrivate = false;
    public void bad() throws Throwable
    {
        long data;
        data = Long.MIN_VALUE;
        badPrivate = true;
        badSink(data );
    }
    private void badSink(long data ) throws Throwable
    {
        if (badPrivate)
        {
            long result = (long)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private boolean goodB2G1Private = false;
    private boolean goodB2G2Private = false;
    private boolean goodG2BPrivate = false;
    public void good() throws Throwable
    {
        goodB2G1();
        goodB2G2();
        goodG2B();
    }
    private void goodB2G1() throws Throwable
    {
        long data;
        data = Long.MIN_VALUE;
        goodB2G1Private = false;
        goodB2G1Sink(data );
    }
    private void goodB2G1Sink(long data ) throws Throwable
    {
        if (goodB2G1Private)
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
        data = Long.MIN_VALUE;
        goodB2G2Private = true;
        goodB2G2Sink(data );
    }
    private void goodB2G2Sink(long data ) throws Throwable
    {
        if (goodB2G2Private)
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
    private void goodG2B() throws Throwable
    {
        long data;
        data = 2;
        goodG2BPrivate = true;
        goodG2BSink(data );
    }
    private void goodG2BSink(long data ) throws Throwable
    {
        if (goodG2BPrivate)
        {
            long result = (long)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
