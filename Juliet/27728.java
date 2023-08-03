
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_rand_square_61a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        long data = (new CWE190_Integer_Overflow__long_rand_square_61b()).badSource();
        long result = (long)(data * data);
        IO.writeLine("result: " + result);
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        long data = (new CWE190_Integer_Overflow__long_rand_square_61b()).goodG2BSource();
        long result = (long)(data * data);
        IO.writeLine("result: " + result);
    }
    private void goodB2G() throws Throwable
    {
        long data = (new CWE190_Integer_Overflow__long_rand_square_61b()).goodB2GSource();
        if ((data != Integer.MIN_VALUE) && (data != Long.MIN_VALUE) && (Math.abs(data) <= (long)Math.sqrt(Long.MAX_VALUE)))
        {
            long result = (long)(data * data);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too large to perform squaring.");
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
