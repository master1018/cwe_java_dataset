
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__byte_max_add_42 extends AbstractTestCase
{
    private byte badSource() throws Throwable
    {
        byte data;
        data = Byte.MAX_VALUE;
        return data;
    }
    public void bad() throws Throwable
    {
        byte data = badSource();
        byte result = (byte)(data + 1);
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
        byte result = (byte)(data + 1);
        IO.writeLine("result: " + result);
    }
    private byte goodB2GSource() throws Throwable
    {
        byte data;
        data = Byte.MAX_VALUE;
        return data;
    }
    private void goodB2G() throws Throwable
    {
        byte data = goodB2GSource();
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
