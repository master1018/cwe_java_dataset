
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
public class CWE191_Integer_Underflow__byte_min_sub_17 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        byte data;
        data = Byte.MIN_VALUE;
        for (int j = 0; j < 1; j++)
        {
            byte result = (byte)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B() throws Throwable
    {
        byte data;
        data = 2;
        for (int j = 0; j < 1; j++)
        {
            byte result = (byte)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodB2G() throws Throwable
    {
        byte data;
        data = Byte.MIN_VALUE;
        for (int k = 0; k < 1; k++)
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
