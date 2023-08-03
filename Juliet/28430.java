
package testcases.CWE325_Missing_Required_Cryptographic_Step;
import testcasesupport.*;
import java.security.MessageDigest;
public class CWE325_Missing_Required_Cryptographic_Step__MessageDigest_update_08 extends AbstractTestCase
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
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            IO.writeLine(IO.toHex(messageDigest.digest()));
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
            final String HASH_INPUT = "ABCDEFG123456";
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(HASH_INPUT.getBytes("UTF-8"));
            IO.writeLine(IO.toHex(messageDigest.digest()));
        }
    }
    private void good2() throws Throwable
    {
        if (privateReturnsTrue())
        {
            final String HASH_INPUT = "ABCDEFG123456";
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(HASH_INPUT.getBytes("UTF-8"));
            IO.writeLine(IO.toHex(messageDigest.digest()));
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
