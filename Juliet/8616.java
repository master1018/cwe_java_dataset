
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
public class CWE191_Integer_Underflow__byte_min_multiply_53a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        byte data;
        data = Byte.MIN_VALUE;
        (new CWE191_Integer_Underflow__byte_min_multiply_53b()).badSink(data );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        byte data;
        data = 2;
        (new CWE191_Integer_Underflow__byte_min_multiply_53b()).goodG2BSink(data );
    }
    private void goodB2G() throws Throwable
    {
        byte data;
        data = Byte.MIN_VALUE;
        (new CWE191_Integer_Underflow__byte_min_multiply_53b()).goodB2GSink(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
