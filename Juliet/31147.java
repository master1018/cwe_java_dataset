
package testcases.CWE319_Cleartext_Tx_Sensitive_Info;
import testcasesupport.*;
import java.net.PasswordAuthentication;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
public class CWE319_Cleartext_Tx_Sensitive_Info__URLConnection_passwordAuth_67b
{
    public void badSink(CWE319_Cleartext_Tx_Sensitive_Info__URLConnection_passwordAuth_67a.Container passwordContainer ) throws Throwable
    {
        String password = passwordContainer.containerOne;
        if (password != null)
        {
            PasswordAuthentication credentials = new PasswordAuthentication("user", password.toCharArray());
            IO.writeLine(credentials.toString());
        }
    }
    public void goodG2BSink(CWE319_Cleartext_Tx_Sensitive_Info__URLConnection_passwordAuth_67a.Container passwordContainer ) throws Throwable
    {
        String password = passwordContainer.containerOne;
        if (password != null)
        {
            PasswordAuthentication credentials = new PasswordAuthentication("user", password.toCharArray());
            IO.writeLine(credentials.toString());
        }
    }
    public void goodB2GSink(CWE319_Cleartext_Tx_Sensitive_Info__URLConnection_passwordAuth_67a.Container passwordContainer ) throws Throwable
    {
        String password = passwordContainer.containerOne;
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
