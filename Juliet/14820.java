
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_max_square_52a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        long data;
        data = Long.MAX_VALUE;
        (new CWE190_Integer_Overflow__long_max_square_52b()).badSink(data );
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
        (new CWE190_Integer_Overflow__long_max_square_52b()).goodG2BSink(data );
    }
    private void goodB2G() throws Throwable
    {
        long data;
        data = Long.MAX_VALUE;
        (new CWE190_Integer_Overflow__long_max_square_52b()).goodB2GSink(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
