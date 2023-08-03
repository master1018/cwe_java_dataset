
package testcases.CWE190_Integer_Overflow.s05;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_max_square_68a extends AbstractTestCase
{
    public static short data;
    public void bad() throws Throwable
    {
        data = Short.MAX_VALUE;
        (new CWE190_Integer_Overflow__short_max_square_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        data = 2;
        (new CWE190_Integer_Overflow__short_max_square_68b()).goodG2BSink();
    }
    private void goodB2G() throws Throwable
    {
        data = Short.MAX_VALUE;
        (new CWE190_Integer_Overflow__short_max_square_68b()).goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
