
package testcases.CWE197_Numeric_Truncation_Error.s01;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__int_large_to_short_53a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = Short.MAX_VALUE + 5;
        (new CWE197_Numeric_Truncation_Error__int_large_to_short_53b()).badSink(data );
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        (new CWE197_Numeric_Truncation_Error__int_large_to_short_53b()).goodG2BSink(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
