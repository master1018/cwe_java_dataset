
package testcases.CWE338_Weak_PRNG;
import testcasesupport.*;
import java.security.SecureRandom;
import java.util.Random;
public class CWE338_Weak_PRNG__util_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        while(true)
        {
            Random random = new Random();
            IO.writeLine("" + random.nextInt());
            break;
        }
    }
    private void good1() throws Throwable
    {
        while(true)
        {
            SecureRandom secureRandom = new SecureRandom();
            IO.writeLine("" + secureRandom.nextDouble());
            break;
        }
    }
    public void good() throws Throwable
    {
        good1();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
