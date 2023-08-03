
package testcases.CWE328_Reversible_One_Way_Hash;
import testcasesupport.*;
import java.security.MessageDigest;
public class CWE328_Reversible_One_Way_Hash__MD5_14 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (IO.staticFive == 5)
        {
            String input = "Test Input";
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] hashValue = messageDigest.digest(input.getBytes("UTF-8")); 
            IO.writeLine(IO.toHex(hashValue));
        }
    }
    private void good1() throws Throwable
    {
        if (IO.staticFive != 5)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            String input = "Test Input";
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] hashValue = messageDigest.digest(input.getBytes("UTF-8")); 
            IO.writeLine(IO.toHex(hashValue));
        }
    }
    private void good2() throws Throwable
    {
        if (IO.staticFive == 5)
        {
            String input = "Test Input";
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] hashValue = messageDigest.digest(input.getBytes("UTF-8")); 
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
