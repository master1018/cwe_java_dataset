
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
public class CWE191_Integer_Underflow__byte_min_sub_21 extends AbstractTestCase
{
    private boolean badPrivate = false;
    public void bad() throws Throwable
    {
        byte data;
        data = Byte.MIN_VALUE;
        badPrivate = true;
        badSink(data );
    }
    private void badSink(byte data ) throws Throwable
    {
        if (badPrivate)
        {
            byte result = (byte)(data - 1);
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
        byte data;
        data = Byte.MIN_VALUE;
        goodB2G1Private = false;
        goodB2G1Sink(data );
    }
    private void goodB2G1Sink(byte data ) throws Throwable
    {
        if (goodB2G1Private)
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
        data = Byte.MIN_VALUE;
        goodB2G2Private = true;
        goodB2G2Sink(data );
    }
    private void goodB2G2Sink(byte data ) throws Throwable
    {
        if (goodB2G2Private)
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
    private void goodG2B() throws Throwable
    {
        byte data;
        data = 2;
        goodG2BPrivate = true;
        goodG2BSink(data );
    }
    private void goodG2BSink(byte data ) throws Throwable
    {
        if (goodG2BPrivate)
        {
            byte result = (byte)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
