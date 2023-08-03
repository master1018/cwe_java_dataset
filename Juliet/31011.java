
package testcases.CWE789_Uncontrolled_Mem_Alloc.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE789_Uncontrolled_Mem_Alloc__random_ArrayList_68a extends AbstractTestCase
{
    public static int data;
    public void bad() throws Throwable
    {
        data = (new SecureRandom()).nextInt();
        (new CWE789_Uncontrolled_Mem_Alloc__random_ArrayList_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        data = 2;
        (new CWE789_Uncontrolled_Mem_Alloc__random_ArrayList_68b()).goodG2BSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
