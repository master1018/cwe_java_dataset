
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__long_min_multiply_41 extends AbstractTestCase
{
    private void badSink(long data ) throws Throwable
    {
        if(data < 0) 
        {
            long result = (long)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void bad() throws Throwable
    {
        long data;
        data = Long.MIN_VALUE;
        badSink(data  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2BSink(long data ) throws Throwable
    {
        if(data < 0) 
        {
            long result = (long)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B() throws Throwable
    {
        long data;
        data = 2;
        goodG2BSink(data  );
    }
    private void goodB2GSink(long data ) throws Throwable
    {
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
    private void goodB2G() throws Throwable
    {
        long data;
        data = Long.MIN_VALUE;
        goodB2GSink(data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
