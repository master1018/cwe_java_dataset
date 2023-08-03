
package testcases.CWE336_Same_Seed_in_PRNG;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE336_Same_Seed_in_PRNG__basic_13 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (IO.STATIC_FINAL_FIVE == 5)
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
        if (IO.STATIC_FINAL_FIVE != 5)
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
        if (IO.STATIC_FINAL_FIVE == 5)
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
