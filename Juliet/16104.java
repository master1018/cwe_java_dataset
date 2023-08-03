
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__short_min_sub_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = Short.MIN_VALUE;
        }
        else
        {
            data = 2;
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            short result = (short)(data - 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            if (data > Short.MIN_VALUE)
            {
                short result = (short)(data - 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform subtraction.");
            }
        }
    }
    private void goodG2B() throws Throwable
    {
        short data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = 2;
        }
        else
        {
            data = 2;
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            short result = (short)(data - 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            short result = (short)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodB2G() throws Throwable
    {
        short data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = Short.MIN_VALUE;
        }
        else
        {
            data = Short.MIN_VALUE;
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            if (data > Short.MIN_VALUE)
            {
                short result = (short)(data - 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform subtraction.");
            }
        }
        else
        {
            if (data > Short.MIN_VALUE)
            {
                short result = (short)(data - 1);
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
