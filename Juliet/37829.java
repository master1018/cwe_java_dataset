
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__short_min_multiply_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short data;
        data = Short.MIN_VALUE;
        if(data < 0) 
        {
            short result = (short)(data * 2);
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
        short data;
        data = 2;
        if(data < 0) 
        {
            short result = (short)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    private void goodB2G() throws Throwable
    {
        short data;
        data = Short.MIN_VALUE;
        if(data < 0) 
        {
            if (data > (Short.MIN_VALUE/2))
            {
                short result = (short)(data * 2);
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
