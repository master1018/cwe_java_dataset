
package testcases.CWE190_Integer_Overflow.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_max_add_07 extends AbstractTestCase
{
    private int privateFive = 5;
    public void bad() throws Throwable
    {
        int data;
        if (privateFive==5)
        {
            data = Integer.MAX_VALUE;
        }
        else
        {
            data = 0;
        }
        if (privateFive==5)
        {
            int result = (int)(data + 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        if (privateFive!=5)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        if (privateFive==5)
        {
            int result = (int)(data + 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        if (privateFive==5)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        if (privateFive==5)
        {
            int result = (int)(data + 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodB2G1() throws Throwable
    {
        int data;
        if (privateFive==5)
        {
            data = Integer.MAX_VALUE;
        }
        else
        {
            data = 0;
        }
        if (privateFive!=5)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if (data < Integer.MAX_VALUE)
            {
                int result = (int)(data + 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform addition.");
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        int data;
        if (privateFive==5)
        {
            data = Integer.MAX_VALUE;
        }
        else
        {
            data = 0;
        }
        if (privateFive==5)
        {
            if (data < Integer.MAX_VALUE)
            {
                int result = (int)(data + 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform addition.");
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
