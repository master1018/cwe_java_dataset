
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__byte_rand_multiply_68a extends AbstractTestCase
{
    public static byte data;
    public void bad() throws Throwable
    {
        data = (byte)((new java.security.SecureRandom()).nextInt(1+Byte.MAX_VALUE-Byte.MIN_VALUE) + Byte.MIN_VALUE);
        (new CWE190_Integer_Overflow__byte_rand_multiply_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        data = 2;
        (new CWE190_Integer_Overflow__byte_rand_multiply_68b()).goodG2BSink();
    }
    private void goodB2G() throws Throwable
    {
        data = (byte)((new java.security.SecureRandom()).nextInt(1+Byte.MAX_VALUE-Byte.MIN_VALUE) + Byte.MIN_VALUE);
        (new CWE190_Integer_Overflow__byte_rand_multiply_68b()).goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
