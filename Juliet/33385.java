
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__short_large_41 extends AbstractTestCase
{
    private void badSink(short data ) throws Throwable
    {
        {
            IO.writeLine((byte)data);
        }
    }
    public void bad() throws Throwable
    {
        short data;
        data = Byte.MAX_VALUE + 5;
        badSink(data  );
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2BSink(short data ) throws Throwable
    {
        {
            IO.writeLine((byte)data);
        }
    }
    private void goodG2B() throws Throwable
    {
        short data;
        data = 2;
        goodG2BSink(data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
