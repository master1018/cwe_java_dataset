
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__long_min_multiply_71a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        long data;
        data = Long.MIN_VALUE;
        (new CWE191_Integer_Underflow__long_min_multiply_71b()).badSink((Object)data  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        long data;
        data = 2;
        (new CWE191_Integer_Underflow__long_min_multiply_71b()).goodG2BSink((Object)data  );
    }
    private void goodB2G() throws Throwable
    {
        long data;
        data = Long.MIN_VALUE;
        (new CWE191_Integer_Underflow__long_min_multiply_71b()).goodB2GSink((Object)data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
