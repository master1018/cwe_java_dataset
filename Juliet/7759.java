
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__byte_max_add_54a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        byte data;
        data = Byte.MAX_VALUE;
        (new CWE190_Integer_Overflow__byte_max_add_54b()).badSink(data );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        byte data;
        data = 2;
        (new CWE190_Integer_Overflow__byte_max_add_54b()).goodG2BSink(data );
    }
    private void goodB2G() throws Throwable
    {
        byte data;
        data = Byte.MAX_VALUE;
        (new CWE190_Integer_Overflow__byte_max_add_54b()).goodB2GSink(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
