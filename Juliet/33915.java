
package testcases.CWE319_Cleartext_Tx_Sensitive_Info;
import testcasesupport.*;
import java.net.PasswordAuthentication;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
public class CWE319_Cleartext_Tx_Sensitive_Info__connect_tcp_passwordAuth_81_goodG2B extends CWE319_Cleartext_Tx_Sensitive_Info__connect_tcp_passwordAuth_81_base
{
    public void action(String password ) throws Throwable
    {
        if (password != null)
        {
            PasswordAuthentication credentials = new PasswordAuthentication("user", password.toCharArray());
            IO.writeLine(credentials.toString());
        }
    }
}
