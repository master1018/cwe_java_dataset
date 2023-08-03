
package testcases.CWE191_Integer_Underflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE191_Integer_Underflow__int_min_multiply_71a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = Integer.MIN_VALUE;
        (new CWE191_Integer_Underflow__int_min_multiply_71b()).badSink((Object)data  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        (new CWE191_Integer_Underflow__int_min_multiply_71b()).goodG2BSink((Object)data  );
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = Integer.MIN_VALUE;
        (new CWE191_Integer_Underflow__int_min_multiply_71b()).goodB2GSink((Object)data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
