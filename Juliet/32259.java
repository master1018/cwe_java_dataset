
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
public class CWE191_Integer_Underflow__byte_rand_multiply_61a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        byte data = (new CWE191_Integer_Underflow__byte_rand_multiply_61b()).badSource();
        if(data < 0) 
        {
            byte result = (byte)(data * 2);
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
        byte data = (new CWE191_Integer_Underflow__byte_rand_multiply_61b()).goodG2BSource();
        if(data < 0) 
        {
            byte result = (byte)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    private void goodB2G() throws Throwable
    {
        byte data = (new CWE191_Integer_Underflow__byte_rand_multiply_61b()).goodB2GSource();
        if(data < 0) 
        {
            if (data > (Byte.MIN_VALUE/2))
            {
                byte result = (byte)(data * 2);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform multiplication.");
            }
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
