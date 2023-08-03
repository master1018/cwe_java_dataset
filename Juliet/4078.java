
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__short_large_66a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short data;
        data = Byte.MAX_VALUE + 5;
        short[] dataArray = new short[5];
        dataArray[2] = data;
        (new CWE197_Numeric_Truncation_Error__short_large_66b()).badSink(dataArray  );
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        short data;
        data = 2;
        short[] dataArray = new short[5];
        dataArray[2] = data;
        (new CWE197_Numeric_Truncation_Error__short_large_66b()).goodG2BSink(dataArray  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
