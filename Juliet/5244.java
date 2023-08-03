
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__short_rand_multiply_51a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short data;
        data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
        (new CWE191_Integer_Underflow__short_rand_multiply_51b()).badSink(data  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        short data;
        data = 2;
        (new CWE191_Integer_Underflow__short_rand_multiply_51b()).goodG2BSink(data  );
    }
    private void goodB2G() throws Throwable
    {
        short data;
        data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
        (new CWE191_Integer_Underflow__short_rand_multiply_51b()).goodB2GSink(data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
