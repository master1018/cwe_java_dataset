
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__byte_max_add_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        byte data;
        while (true)
        {
            data = Byte.MAX_VALUE;
            break;
        }
        while (true)
        {
            byte result = (byte)(data + 1);
            IO.writeLine("result: " + result);
            break;
        }
    }
    private void goodG2B() throws Throwable
    {
        byte data;
        while (true)
        {
            data = 2;
            break;
        }
        while (true)
        {
            byte result = (byte)(data + 1);
            IO.writeLine("result: " + result);
            break;
        }
    }
    private void goodB2G() throws Throwable
    {
        byte data;
        while (true)
        {
            data = Byte.MAX_VALUE;
            break;
        }
        while (true)
        {
            if (data < Byte.MAX_VALUE)
            {
                byte result = (byte)(data + 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform addition.");
            }
            break;
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
