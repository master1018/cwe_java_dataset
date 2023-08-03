
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE369_Divide_by_Zero__float_random_modulo_68a extends AbstractTestCase
{
    public static float data;
    public void bad() throws Throwable
    {
        SecureRandom secureRandom = new SecureRandom();
        data = secureRandom.nextFloat();
        (new CWE369_Divide_by_Zero__float_random_modulo_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        data = 2.0f;
        (new CWE369_Divide_by_Zero__float_random_modulo_68b()).goodG2BSink();
    }
    private void goodB2G() throws Throwable
    {
        SecureRandom secureRandom = new SecureRandom();
        data = secureRandom.nextFloat();
        (new CWE369_Divide_by_Zero__float_random_modulo_68b()).goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
