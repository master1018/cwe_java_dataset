
package testcases.CWE325_Missing_Required_Cryptographic_Step;
import testcasesupport.*;
import java.security.MessageDigest;
public class CWE325_Missing_Required_Cryptographic_Step__MessageDigest_update_17 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        for(int j = 0; j < 1; j++)
        {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            IO.writeLine(IO.toHex(messageDigest.digest()));
        }
    }
    private void good1() throws Throwable
    {
        for(int k = 0; k < 1; k++)
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
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
