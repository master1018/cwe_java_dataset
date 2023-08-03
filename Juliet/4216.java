
package testcases.CWE190_Integer_Overflow.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE190_Integer_Overflow__int_random_multiply_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = (new SecureRandom()).nextInt();
        CWE190_Integer_Overflow__int_random_multiply_81_base baseObject = new CWE190_Integer_Overflow__int_random_multiply_81_bad();
        baseObject.action(data );
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
        CWE190_Integer_Overflow__int_random_multiply_81_base baseObject = new CWE190_Integer_Overflow__int_random_multiply_81_goodG2B();
        baseObject.action(data );
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = (new SecureRandom()).nextInt();
        CWE190_Integer_Overflow__int_random_multiply_81_base baseObject = new CWE190_Integer_Overflow__int_random_multiply_81_goodB2G();
        baseObject.action(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
