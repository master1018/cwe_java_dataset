
package testcases.CWE369_Divide_by_Zero.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE369_Divide_by_Zero__int_random_divide_68a extends AbstractTestCase
{
    public static int data;
    public void bad() throws Throwable
    {
        data = (new SecureRandom()).nextInt();
        (new CWE369_Divide_by_Zero__int_random_divide_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        data = 2;
        (new CWE369_Divide_by_Zero__int_random_divide_68b()).goodG2BSink();
    }
    private void goodB2G() throws Throwable
    {
        data = (new SecureRandom()).nextInt();
        (new CWE369_Divide_by_Zero__int_random_divide_68b()).goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
