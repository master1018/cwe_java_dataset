
package testcases.CWE328_Reversible_One_Way_Hash;
import testcasesupport.*;
import java.security.MessageDigest;
public class CWE328_Reversible_One_Way_Hash__MD2_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String input = "Test Input";
        MessageDigest messageDigest = MessageDigest.getInstance("MD2");
        byte[] hashValue = messageDigest.digest(input.getBytes("UTF-8")); 
        IO.writeLine(IO.toHex(hashValue));
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
    {
        String input = "Test Input";
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        byte[] hashValue = messageDigest.digest(input.getBytes("UTF-8")); 
        IO.writeLine(IO.toHex(hashValue));
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
