
package testcases.CWE327_Use_Broken_Crypto;
import testcasesupport.*;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
public class CWE327_Use_Broken_Crypto__3DES_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        final String CIPHER_INPUT = "ABCDEFG123456";
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
        keyGenerator.init(112);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] byteKey = secretKey.getEncoded();
        SecretKeySpec secretKeySpec = new SecretKeySpec(byteKey, "DESede");
        Cipher tripleDesCipher = Cipher.getInstance("DESede");
        tripleDesCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encrypted = tripleDesCipher.doFinal(CIPHER_INPUT.getBytes("UTF-8"));
        IO.writeLine(IO.toHex(encrypted));
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
    {
        final String CIPHER_INPUT = "ABCDEFG123456";
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] byteKey = secretKey.getEncoded();
        SecretKeySpec secretKeySpec = new SecretKeySpec(byteKey, "AES");
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encrypted = aesCipher.doFinal(CIPHER_INPUT.getBytes("UTF-8"));
        IO.writeLine(IO.toHex(encrypted));
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}