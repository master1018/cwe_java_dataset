
package testcases.CWE190_Integer_Overflow.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_max_square_21 extends AbstractTestCase
{
    private boolean badPrivate = false;
    public void bad() throws Throwable
    {
        int data;
        data = Integer.MAX_VALUE;
        badPrivate = true;
        badSink(data );
    }
    private void badSink(int data ) throws Throwable
    {
        if (badPrivate)
        {
            int result = (int)(data * data);
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
        int data;
        data = Integer.MAX_VALUE;
        goodB2G1Private = false;
        goodB2G1Sink(data );
    }
    private void goodB2G1Sink(int data ) throws Throwable
    {
        if (goodB2G1Private)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if ((data != Integer.MIN_VALUE) && (data != Long.MIN_VALUE) && (Math.abs(data) <= (long)Math.sqrt(Integer.MAX_VALUE)))
            {
                int result = (int)(data * data);
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
        int data;
        data = Integer.MAX_VALUE;
        goodB2G2Private = true;
        goodB2G2Sink(data );
    }
    private void goodB2G2Sink(int data ) throws Throwable
    {
        if (goodB2G2Private)
        {
            if ((data != Integer.MIN_VALUE) && (data != Long.MIN_VALUE) && (Math.abs(data) <= (long)Math.sqrt(Integer.MAX_VALUE)))
            {
                int result = (int)(data * data);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform squaring.");
            }
        }
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        goodG2BPrivate = true;
        goodG2BSink(data );
    }
    private void goodG2BSink(int data ) throws Throwable
    {
        if (goodG2BPrivate)
        {
            int result = (int)(data * data);
            IO.writeLine("result: " + result);
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
