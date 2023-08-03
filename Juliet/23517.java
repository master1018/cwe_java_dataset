
package testcases.CWE191_Integer_Underflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE191_Integer_Underflow__int_min_multiply_68a extends AbstractTestCase
{
    public static int data;
    public void bad() throws Throwable
    {
        data = Integer.MIN_VALUE;
        (new CWE191_Integer_Underflow__int_min_multiply_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        data = 2;
        (new CWE191_Integer_Underflow__int_min_multiply_68b()).goodG2BSink();
    }
    private void goodB2G() throws Throwable
    {
        data = Integer.MIN_VALUE;
        (new CWE191_Integer_Underflow__int_min_multiply_68b()).goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
