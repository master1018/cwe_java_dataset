
package testcases.CWE190_Integer_Overflow.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_max_add_53a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = Integer.MAX_VALUE;
        (new CWE190_Integer_Overflow__int_max_add_53b()).badSink(data );
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
        (new CWE190_Integer_Overflow__int_max_add_53b()).goodG2BSink(data );
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = Integer.MAX_VALUE;
        (new CWE190_Integer_Overflow__int_max_add_53b()).goodB2GSink(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
