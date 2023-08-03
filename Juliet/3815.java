
package testcases.CWE338_Weak_PRNG;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE338_Weak_PRNG__math_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        IO.writeLine("" + Math.random());
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
    {
        SecureRandom secureRandom = new SecureRandom();
        IO.writeLine("" + secureRandom.nextDouble());
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
