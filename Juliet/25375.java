
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
public class CWE191_Integer_Underflow__byte_min_sub_45 extends AbstractTestCase
{
    private byte dataBad;
    private byte dataGoodG2B;
    private byte dataGoodB2G;
    private void badSink() throws Throwable
    {
        byte data = dataBad;
        byte result = (byte)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void bad() throws Throwable
    {
        byte data;
        data = Byte.MIN_VALUE;
        dataBad = data;
        badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2BSink() throws Throwable
    {
        byte data = dataGoodG2B;
        byte result = (byte)(data - 1);
        IO.writeLine("result: " + result);
    }
    private void goodG2B() throws Throwable
    {
        byte data;
        data = 2;
        dataGoodG2B = data;
        goodG2BSink();
    }
    private void goodB2GSink() throws Throwable
    {
        byte data = dataGoodB2G;
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
    private void goodB2G() throws Throwable
    {
        byte data;
        data = Byte.MIN_VALUE;
        dataGoodB2G = data;
        goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
