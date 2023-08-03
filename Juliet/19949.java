
package testcases.CWE319_Cleartext_Tx_Sensitive_Info;
import testcasesupport.*;
import java.net.PasswordAuthentication;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
public class CWE319_Cleartext_Tx_Sensitive_Info__listen_tcp_passwordAuth_81_goodB2G extends CWE319_Cleartext_Tx_Sensitive_Info__listen_tcp_passwordAuth_81_base
{
    public void action(String password ) throws Throwable
    {
        if (password != null)
        {
            {
                Cipher aesCipher = Cipher.getInstance("AES");
                SecretKeySpec secretKeySpec = new SecretKeySpec("ABCDEFGHABCDEFGH".getBytes("UTF-8"), "AES");
                aesCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
                password = new String(aesCipher.doFinal(password.getBytes("UTF-8")), "UTF-8");
            }
            PasswordAuthentication credentials = new PasswordAuthentication("user", password.toCharArray());
            IO.writeLine(credentials.toString());
        }
    }
}
