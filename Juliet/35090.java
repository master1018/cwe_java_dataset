
package testcases.CWE338_Weak_PRNG;
import testcasesupport.*;
import java.security.SecureRandom;
import java.util.Random;
public class CWE338_Weak_PRNG__util_11 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (IO.staticReturnsTrue())
        {
            Random random = new Random();
            IO.writeLine("" + random.nextInt());
        }
    }
    private void good1() throws Throwable
    {
        if (IO.staticReturnsFalse())
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            SecureRandom secureRandom = new SecureRandom();
            IO.writeLine("" + secureRandom.nextDouble());
        }
    }
    private void good2() throws Throwable
    {
        if (IO.staticReturnsTrue())
        {
            SecureRandom secureRandom = new SecureRandom();
            IO.writeLine("" + secureRandom.nextDouble());
        }
    }
    public void good() throws Throwable
    {
        good1();
        good2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
