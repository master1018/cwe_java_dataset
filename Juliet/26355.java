
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_max_add_61a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short data = (new CWE190_Integer_Overflow__short_max_add_61b()).badSource();
        short result = (short)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        short data = (new CWE190_Integer_Overflow__short_max_add_61b()).goodG2BSource();
        short result = (short)(data + 1);
        IO.writeLine("result: " + result);
    }
    private void goodB2G() throws Throwable
    {
        short data = (new CWE190_Integer_Overflow__short_max_add_61b()).goodB2GSource();
        if (data < Short.MAX_VALUE)
        {
            short result = (short)(data + 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too large to perform addition.");
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
