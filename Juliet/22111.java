
package testcases.CWE338_Weak_PRNG;
import testcasesupport.*;
import java.security.SecureRandom;
import java.util.Random;
public class CWE338_Weak_PRNG__util_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        Random random = new Random();
        IO.writeLine("" + random.nextInt());
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
