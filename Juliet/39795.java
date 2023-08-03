
package testcases.CWE197_Numeric_Truncation_Error.s01;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__int_large_to_byte_68a extends AbstractTestCase
{
    public static int data;
    public void bad() throws Throwable
    {
        data = Short.MAX_VALUE + 5;
        (new CWE197_Numeric_Truncation_Error__int_large_to_byte_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        data = 2;
        (new CWE197_Numeric_Truncation_Error__int_large_to_byte_68b()).goodG2BSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
