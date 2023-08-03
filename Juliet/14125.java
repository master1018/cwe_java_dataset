
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__short_large_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short data;
        data = Byte.MAX_VALUE + 5;
        CWE197_Numeric_Truncation_Error__short_large_81_base baseObject = new CWE197_Numeric_Truncation_Error__short_large_81_bad();
        baseObject.action(data );
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        short data;
        data = 2;
        CWE197_Numeric_Truncation_Error__short_large_81_base baseObject = new CWE197_Numeric_Truncation_Error__short_large_81_goodG2B();
        baseObject.action(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
