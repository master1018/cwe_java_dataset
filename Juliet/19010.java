
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_max_add_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        long data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = Long.MAX_VALUE;
        }
        else
        {
            data = 2;
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            long result = (long)(data + 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            if (data < Long.MAX_VALUE)
            {
                long result = (long)(data + 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform addition.");
            }
        }
    }
    private void goodG2B() throws Throwable
    {
        long data;
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
            long result = (long)(data + 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            long result = (long)(data + 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodB2G() throws Throwable
    {
        long data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = Long.MAX_VALUE;
        }
        else
        {
            data = Long.MAX_VALUE;
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            if (data < Long.MAX_VALUE)
            {
                long result = (long)(data + 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform addition.");
            }
        }
        else
        {
            if (data < Long.MAX_VALUE)
            {
                long result = (long)(data + 1);
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
        goodG2B();
        goodB2G();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
