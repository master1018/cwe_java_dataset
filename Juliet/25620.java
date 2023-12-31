
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__byte_rand_add_31 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        byte dataCopy;
        {
            byte data;
            data = (byte)((new java.security.SecureRandom()).nextInt(1+Byte.MAX_VALUE-Byte.MIN_VALUE) + Byte.MIN_VALUE);
            dataCopy = data;
        }
        {
            byte data = dataCopy;
            byte result = (byte)(data + 1);
            IO.writeLine("result: " + result);
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        byte dataCopy;
        {
            byte data;
            data = 2;
            dataCopy = data;
        }
        {
            byte data = dataCopy;
            byte result = (byte)(data + 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodB2G() throws Throwable
    {
        byte dataCopy;
        {
            byte data;
            data = (byte)((new java.security.SecureRandom()).nextInt(1+Byte.MAX_VALUE-Byte.MIN_VALUE) + Byte.MIN_VALUE);
            dataCopy = data;
        }
        {
            byte data = dataCopy;
            if (data < Byte.MAX_VALUE)
            {
                byte result = (byte)(data + 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform addition.");
            }
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
