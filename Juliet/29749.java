
package testcases.CWE190_Integer_Overflow.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE190_Integer_Overflow__int_random_multiply_68a extends AbstractTestCase
{
    public static int data;
    public void bad() throws Throwable
    {
        data = (new SecureRandom()).nextInt();
        (new CWE190_Integer_Overflow__int_random_multiply_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        data = 2;
        (new CWE190_Integer_Overflow__int_random_multiply_68b()).goodG2BSink();
    }
    private void goodB2G() throws Throwable
    {
        data = (new SecureRandom()).nextInt();
        (new CWE190_Integer_Overflow__int_random_multiply_68b()).goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
