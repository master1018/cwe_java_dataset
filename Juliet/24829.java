
package testcases.CWE321_Hard_Coded_Cryptographic_Key;
import testcasesupport.*;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
public class CWE321_Hard_Coded_Cryptographic_Key__basic_67b
{
    public void badSink(CWE321_Hard_Coded_Cryptographic_Key__basic_67a.Container dataContainer ) throws Throwable
    {
        String data = dataContainer.containerOne;
        if (data != null)
        {
            String stringToEncrypt = "Super secret Squirrel";
            byte[] byteStringToEncrypt = stringToEncrypt.getBytes("UTF-8");
            SecretKeySpec secretKeySpec = new SecretKeySpec(data.getBytes("UTF-8"), "AES");
            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] byteCipherText = aesCipher.doFinal(byteStringToEncrypt);
            IO.writeLine(IO.toHex(byteCipherText)); 
        }
    }
    public void goodG2BSink(CWE321_Hard_Coded_Cryptographic_Key__basic_67a.Container dataContainer ) throws Throwable
    {
        String data = dataContainer.containerOne;
        if (data != null)
        {
            String stringToEncrypt = "Super secret Squirrel";
            byte[] byteStringToEncrypt = stringToEncrypt.getBytes("UTF-8");
            SecretKeySpec secretKeySpec = new SecretKeySpec(data.getBytes("UTF-8"), "AES");
            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] byteCipherText = aesCipher.doFinal(byteStringToEncrypt);
            IO.writeLine(IO.toHex(byteCipherText)); 
        }
    }
}
