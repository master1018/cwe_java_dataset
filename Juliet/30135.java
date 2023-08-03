
package testcases.CWE759_Unsalted_One_Way_Hash;
import testcasesupport.*;
import java.security.MessageDigest;
import java.security.SecureRandom;
public class CWE759_Unsalted_One_Way_Hash__basic_10 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (IO.staticTrue)
        {
            MessageDigest hash = MessageDigest.getInstance("SHA-512");
            byte[] hashValue = hash.digest("hash me".getBytes("UTF-8"));
            IO.writeLine(IO.toHex(hashValue));
        }
    }
    private void good1() throws Throwable
    {
        if (IO.staticFalse)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            MessageDigest hash = MessageDigest.getInstance("SHA-512");
            SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
            hash.update(prng.generateSeed(32));
            byte[] hashValue = hash.digest("hash me".getBytes("UTF-8"));
            IO.writeLine(IO.toHex(hashValue));
        }
    }
    private void good2() throws Throwable
    {
        if (IO.staticTrue)
        {
            MessageDigest hash = MessageDigest.getInstance("SHA-512");
            SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
            hash.update(prng.generateSeed(32));
            byte[] hashValue = hash.digest("hash me".getBytes("UTF-8"));
            IO.writeLine(IO.toHex(hashValue));
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
