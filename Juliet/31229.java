
package testcases.CWE329_Not_Using_Random_IV_with_CBC_Mode;
import testcasesupport.*;
import javax.servlet.http.*;
import java.io.*;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
public class CWE329_Not_Using_Random_IV_with_CBC_Mode__basic_17 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        for(int j = 0; j < 1; j++)
        {
            byte[] text = "asdf".getBytes("UTF-8");
            byte[] initializationVector =
            {
                0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
                0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00
            };
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            SecretKey key = keyGenerator.generateKey();
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initializationVector);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
            IO.writeLine(IO.toHex(cipher.doFinal(text)));
        }
    }
    private void good1() throws Throwable
    {
        for(int k = 0; k < 1; k++)
        {
            byte[] text = "asdf".getBytes("UTF-8");
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            SecretKey key = keyGenerator.generateKey();
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            int blockSize = cipher.getBlockSize();
            byte[] initializationVector = new byte[blockSize];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(initializationVector);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initializationVector);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
            IO.writeLine(IO.toHex(cipher.doFinal(text)));
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
