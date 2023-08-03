
package testcases.CWE197_Numeric_Truncation_Error.s01;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__int_large_to_short_45 extends AbstractTestCase
{
    private int dataBad;
    private int dataGoodG2B;
    private void badSink() throws Throwable
    {
        int data = dataBad;
        {
            IO.writeLine((short)data);
        }
    }
    public void bad() throws Throwable
    {
        int data;
        data = Short.MAX_VALUE + 5;
        dataBad = data;
        badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2BSink() throws Throwable
    {
        int data = dataGoodG2B;
        {
            IO.writeLine((short)data);
        }
    }
    private void goodG2B() throws Throwable
    {
        int data;
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
