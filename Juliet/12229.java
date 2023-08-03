
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_max_add_68a extends AbstractTestCase
{
    public static long data;
    public void bad() throws Throwable
    {
        data = Long.MAX_VALUE;
        (new CWE190_Integer_Overflow__long_max_add_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        data = 2;
        (new CWE190_Integer_Overflow__long_max_add_68b()).goodG2BSink();
    }
    private void goodB2G() throws Throwable
    {
        data = Long.MAX_VALUE;
        (new CWE190_Integer_Overflow__long_max_add_68b()).goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
