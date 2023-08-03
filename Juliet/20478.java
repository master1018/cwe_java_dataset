
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
public class CWE191_Integer_Underflow__byte_rand_sub_42 extends AbstractTestCase
{
    private byte badSource() throws Throwable
    {
        byte data;
        data = (byte)((new java.security.SecureRandom()).nextInt(1+Byte.MAX_VALUE-Byte.MIN_VALUE) + Byte.MIN_VALUE);
        return data;
    }
    public void bad() throws Throwable
    {
        byte data = badSource();
        byte result = (byte)(data - 1);
        IO.writeLine("result: " + result);
    }
    private byte goodG2BSource() throws Throwable
    {
        byte data;
        data = 2;
        return data;
    }
    private void goodG2B() throws Throwable
    {
        byte data = goodG2BSource();
        byte result = (byte)(data - 1);
        IO.writeLine("result: " + result);
    }
    private byte goodB2GSource() throws Throwable
    {
        byte data;
        data = (byte)((new java.security.SecureRandom()).nextInt(1+Byte.MAX_VALUE-Byte.MIN_VALUE) + Byte.MIN_VALUE);
        return data;
    }
    private void goodB2G() throws Throwable
    {
        byte data = goodB2GSource();
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
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
