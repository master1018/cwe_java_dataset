
package testcases.CWE336_Same_Seed_in_PRNG;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE336_Same_Seed_in_PRNG__basic_08 extends AbstractTestCase
{
    private boolean privateReturnsTrue()
    {
        return true;
    }
    private boolean privateReturnsFalse()
    {
        return false;
    }
    public void bad() throws Throwable
    {
        if (privateReturnsTrue())
        {
            final byte[] SEED = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05};
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.setSeed(SEED);
            IO.writeLine("" + secureRandom.nextInt()); 
            IO.writeLine("" + secureRandom.nextInt());
        }
    }
    private void good1() throws Throwable
    {
        if (privateReturnsFalse())
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            SecureRandom secureRandom = new SecureRandom();
            IO.writeLine("" + secureRandom.nextInt());
            IO.writeLine("" + secureRandom.nextInt());
        }
    }
    private void good2() throws Throwable
    {
        if (privateReturnsTrue())
        {
            SecureRandom secureRandom = new SecureRandom();
            IO.writeLine("" + secureRandom.nextInt());
            IO.writeLine("" + secureRandom.nextInt());
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