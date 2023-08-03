
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
public class CWE191_Integer_Underflow__byte_min_sub_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        byte data;
        while (true)
        {
            data = Byte.MIN_VALUE;
            break;
        }
        while (true)
        {
            byte result = (byte)(data - 1);
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
            byte result = (byte)(data - 1);
            IO.writeLine("result: " + result);
            break;
        }
    }
    private void goodB2G() throws Throwable
    {
        byte data;
        while (true)
        {
            data = Byte.MIN_VALUE;
            break;
        }
        while (true)
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
