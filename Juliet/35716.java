
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE197_Numeric_Truncation_Error__short_random_45 extends AbstractTestCase
{
    private short dataBad;
    private short dataGoodG2B;
    private void badSink() throws Throwable
    {
        short data = dataBad;
        {
            IO.writeLine((byte)data);
        }
    }
    public void bad() throws Throwable
    {
        short data;
        data = (short)((new SecureRandom()).nextInt(Short.MAX_VALUE + 1));
        dataBad = data;
        badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2BSink() throws Throwable
    {
        short data = dataGoodG2B;
        {
            IO.writeLine((byte)data);
        }
    }
    private void goodG2B() throws Throwable
    {
        short data;
        data = 2;
        dataGoodG2B = data;
        goodG2BSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
