
package testcases.CWE191_Integer_Underflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE191_Integer_Underflow__int_min_sub_03 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        if (5==5)
        {
            data = Integer.MIN_VALUE;
        }
        else
        {
            data = 0;
        }
        if (5==5)
        {
            int result = (int)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        if (5!=5)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        if (5==5)
        {
            int result = (int)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        if (5==5)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        if (5==5)
        {
            int result = (int)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodB2G1() throws Throwable
    {
        int data;
        if (5==5)
        {
            data = Integer.MIN_VALUE;
        }
        else
        {
            data = 0;
        }
        if (5!=5)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if (data > Integer.MIN_VALUE)
            {
                int result = (int)(data - 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform subtraction.");
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        int data;
        if (5==5)
        {
            data = Integer.MIN_VALUE;
        }
        else
        {
            data = 0;
        }
        if (5==5)
        {
            if (data > Integer.MIN_VALUE)
            {
                int result = (int)(data - 1);
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
        goodG2B1();
        goodG2B2();
        goodB2G1();
        goodB2G2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
