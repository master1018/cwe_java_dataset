
package testcases.CWE197_Numeric_Truncation_Error.s01;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__int_large_to_byte_61a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data = (new CWE197_Numeric_Truncation_Error__int_large_to_byte_61b()).badSource();
        {
            IO.writeLine((byte)data);
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        int data = (new CWE197_Numeric_Truncation_Error__int_large_to_byte_61b()).goodG2BSource();
        {
            IO.writeLine((byte)data);
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
