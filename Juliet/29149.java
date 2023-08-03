
package testcases.CWE197_Numeric_Truncation_Error.s01;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__int_large_to_short_31 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int dataCopy;
        {
            int data;
            data = Short.MAX_VALUE + 5;
            dataCopy = data;
        }
        {
            int data = dataCopy;
            {
                IO.writeLine((short)data);
            }
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        int dataCopy;
        {
            int data;
            data = 2;
            dataCopy = data;
        }
        {
            int data = dataCopy;
            {
                IO.writeLine((short)data);
            }
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
