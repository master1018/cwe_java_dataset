
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE191_Integer_Underflow__int_URLConnection_multiply_61a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data = (new CWE191_Integer_Underflow__int_URLConnection_multiply_61b()).badSource();
        if(data < 0) 
        {
            int result = (int)(data * 2);
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
        int data = (new CWE191_Integer_Underflow__int_URLConnection_multiply_61b()).goodG2BSource();
        if(data < 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    private void goodB2G() throws Throwable
    {
        int data = (new CWE191_Integer_Underflow__int_URLConnection_multiply_61b()).goodB2GSource();
        if(data < 0) 
        {
            if (data > (Integer.MIN_VALUE/2))
            {
                int result = (int)(data * 2);
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
