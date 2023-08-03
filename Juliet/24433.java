
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__short_min_sub_51a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short data;
        data = Short.MIN_VALUE;
        (new CWE191_Integer_Underflow__short_min_sub_51b()).badSink(data  );
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
        (new CWE191_Integer_Underflow__short_min_sub_51b()).goodG2BSink(data  );
    }
    private void goodB2G() throws Throwable
    {
        short data;
        data = Short.MIN_VALUE;
        (new CWE191_Integer_Underflow__short_min_sub_51b()).goodB2GSink(data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
