
package testcases.CWE319_Cleartext_Tx_Sensitive_Info;
import testcasesupport.*;
import java.net.PasswordAuthentication;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
public class CWE319_Cleartext_Tx_Sensitive_Info__URLConnection_passwordAuth_61a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String password = (new CWE319_Cleartext_Tx_Sensitive_Info__URLConnection_passwordAuth_61b()).badSource();
        if (password != null)
        {
            PasswordAuthentication credentials = new PasswordAuthentication("user", password.toCharArray());
            IO.writeLine(credentials.toString());
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        String password = (new CWE319_Cleartext_Tx_Sensitive_Info__URLConnection_passwordAuth_61b()).goodG2BSource();
        if (password != null)
        {
            PasswordAuthentication credentials = new PasswordAuthentication("user", password.toCharArray());
            IO.writeLine(credentials.toString());
        }
    }
    private void goodB2G() throws Throwable
    {
        String password = (new CWE319_Cleartext_Tx_Sensitive_Info__URLConnection_passwordAuth_61b()).goodB2GSource();
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
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
