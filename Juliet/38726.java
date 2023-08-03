
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__short_large_21 extends AbstractTestCase
{
    private boolean badPrivate = false;
    public void bad() throws Throwable
    {
        short data;
        badPrivate = true;
        data = bad_source();
        {
            IO.writeLine((byte)data);
        }
    }
    private short bad_source() throws Throwable
    {
        short data;
        if (badPrivate)
        {
            data = Byte.MAX_VALUE + 5;
        }
        else
        {
            data = 0;
        }
        return data;
    }
    private boolean goodG2B1_private = false;
    private boolean goodG2B2_private = false;
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
    }
    private void goodG2B1() throws Throwable
    {
        short data;
        goodG2B1_private = false;
        data = goodG2B1_source();
        {
            IO.writeLine((byte)data);
        }
    }
    private short goodG2B1_source() throws Throwable
    {
        short data = 0;
        if (goodG2B1_private)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        return data;
    }
    private void goodG2B2() throws Throwable
    {
        short data;
        goodG2B2_private = true;
        data = goodG2B2_source();
        {
            IO.writeLine((byte)data);
        }
    }
    private short goodG2B2_source() throws Throwable
    {
        short data = 0;
        if (goodG2B2_private)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        return data;
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
