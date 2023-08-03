
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__long_rand_sub_68a extends AbstractTestCase
{
    public static long data;
    public void bad() throws Throwable
    {
        data = (new java.security.SecureRandom()).nextLong();
        (new CWE191_Integer_Underflow__long_rand_sub_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        data = 2;
        (new CWE191_Integer_Underflow__long_rand_sub_68b()).goodG2BSink();
    }
    private void goodB2G() throws Throwable
    {
        data = (new java.security.SecureRandom()).nextLong();
        (new CWE191_Integer_Underflow__long_rand_sub_68b()).goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
