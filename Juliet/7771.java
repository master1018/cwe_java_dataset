
package testcases.CWE190_Integer_Overflow.s05;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_max_multiply_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short data;
        while (true)
        {
            data = Short.MAX_VALUE;
            break;
        }
        while (true)
        {
            if(data > 0) 
            {
                short result = (short)(data * 2);
                IO.writeLine("result: " + result);
            }
            break;
        }
    }
    private void goodG2B() throws Throwable
    {
        short data;
        while (true)
        {
            data = 2;
            break;
        }
        while (true)
        {
            if(data > 0) 
            {
                short result = (short)(data * 2);
                IO.writeLine("result: " + result);
            }
            break;
        }
    }
    private void goodB2G() throws Throwable
    {
        short data;
        while (true)
        {
            data = Short.MAX_VALUE;
            break;
        }
        while (true)
        {
            if(data > 0) 
            {
                if (data < (Short.MAX_VALUE/2))
                {
                    short result = (short)(data * 2);
                    IO.writeLine("result: " + result);
                }
                else
                {
                    IO.writeLine("data value is too large to perform multiplication.");
                }
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
