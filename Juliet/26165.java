
package testcases.CWE190_Integer_Overflow.s05;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_max_square_51a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short data;
        data = Short.MAX_VALUE;
        (new CWE190_Integer_Overflow__short_max_square_51b()).badSink(data  );
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
        (new CWE190_Integer_Overflow__short_max_square_51b()).goodG2BSink(data  );
    }
    private void goodB2G() throws Throwable
    {
        short data;
        data = Short.MAX_VALUE;
        (new CWE190_Integer_Overflow__short_max_square_51b()).goodB2GSink(data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
