
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__short_large_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short data;
        if (IO.staticReturnsTrueOrFalse())
        {
            data = Byte.MAX_VALUE + 5;
        }
        else
        {
            data = 2;
        }
        {
            IO.writeLine((byte)data);
        }
    }
    private void goodG2B() throws Throwable
    {
        short data;
        if (IO.staticReturnsTrueOrFalse())
        {
            data = 2;
        }
        else
        {
            data = 2;
        }
        {
            IO.writeLine((byte)data);
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
