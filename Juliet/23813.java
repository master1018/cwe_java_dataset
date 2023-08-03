
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_max_add_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short data;
        data = Short.MAX_VALUE;
        CWE190_Integer_Overflow__short_max_add_81_base baseObject = new CWE190_Integer_Overflow__short_max_add_81_bad();
        baseObject.action(data );
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
        CWE190_Integer_Overflow__short_max_add_81_base baseObject = new CWE190_Integer_Overflow__short_max_add_81_goodG2B();
        baseObject.action(data );
    }
    private void goodB2G() throws Throwable
    {
        short data;
        data = Short.MAX_VALUE;
        CWE190_Integer_Overflow__short_max_add_81_base baseObject = new CWE190_Integer_Overflow__short_max_add_81_goodB2G();
        baseObject.action(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
