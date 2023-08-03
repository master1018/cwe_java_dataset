
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
public class CWE191_Integer_Underflow__byte_min_sub_68a extends AbstractTestCase
{
    public static byte data;
    public void bad() throws Throwable
    {
        data = Byte.MIN_VALUE;
        (new CWE191_Integer_Underflow__byte_min_sub_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        data = 2;
        (new CWE191_Integer_Underflow__byte_min_sub_68b()).goodG2BSink();
    }
    private void goodB2G() throws Throwable
    {
        data = Byte.MIN_VALUE;
        (new CWE191_Integer_Underflow__byte_min_sub_68b()).goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
