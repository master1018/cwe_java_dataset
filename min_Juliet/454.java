
package testcases.CWE506_Embedded_Malicious_Code;
import testcasesupport.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.logging.Level;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
public class CWE506_Embedded_Malicious_Code__aes_encrypted_payload_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String payload = "0297b5eb43e3b81f9c737b353c3ade45";
        Cipher aesCipher = Cipher.getInstance("AES");
        SecretKeySpec secretKeySpec = new SecretKeySpec("ABCDEFGHABCDEFGH".getBytes("UTF-8"), "AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        String decodedPayload = "calc.exe";
        try
        {
            Runtime.getRuntime().exec(decodedPayload);
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "Error executing command", exceptIO);
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
