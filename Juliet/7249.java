
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__long_min_multiply_31 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        long dataCopy;
        {
            long data;
            data = Long.MIN_VALUE;
            dataCopy = data;
        }
        {
            long data = dataCopy;
            if(data < 0) 
            {
                long result = (long)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        long dataCopy;
        {
            long data;
            data = 2;
            dataCopy = data;
        }
        {
            long data = dataCopy;
            if(data < 0) 
            {
                long result = (long)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
    }
    private void goodB2G() throws Throwable
    {
        long dataCopy;
        {
            long data;
            data = Long.MIN_VALUE;
            dataCopy = data;
        }
        {
            long data = dataCopy;
            if(data < 0) 
            {
                if (data > (Long.MIN_VALUE/2))
                {
                    long result = (long)(data * 2);
                    IO.writeLine("result: " + result);
                }
                else
                {
                    IO.writeLine("data value is too small to perform multiplication.");
                }
            }
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
