
package testcases.CWE319_Cleartext_Tx_Sensitive_Info;
import testcasesupport.*;
import java.util.HashMap;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.kerberos.KerberosKey;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
public class CWE319_Cleartext_Tx_Sensitive_Info__listen_tcp_kerberosKey_74b
{
    public void badSink(HashMap<Integer,String> passwordHashMap ) throws Throwable
    {
        String password = passwordHashMap.get(2);
        if (password != null)
        {
            KerberosPrincipal principal = new KerberosPrincipal("test");
            KerberosKey key = new KerberosKey(principal, password.toCharArray(), null);
            IO.writeLine(key.toString());
        }
    }
    public void goodG2BSink(HashMap<Integer,String> passwordHashMap ) throws Throwable
    {
        String password = passwordHashMap.get(2);
        if (password != null)
        {
            KerberosPrincipal principal = new KerberosPrincipal("test");
            KerberosKey key = new KerberosKey(principal, password.toCharArray(), null);
            IO.writeLine(key.toString());
        }
    }
    public void goodB2GSink(HashMap<Integer,String> passwordHashMap ) throws Throwable
    {
        String password = passwordHashMap.get(2);
        if (password != null)
        {
            KerberosPrincipal principal = new KerberosPrincipal("test");
            {
                Cipher aesCipher = Cipher.getInstance("AES");
                SecretKeySpec secretKeySpec = new SecretKeySpec("ABCDEFGHABCDEFGH".getBytes("UTF-8"), "AES");
                aesCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
                password = new String(aesCipher.doFinal(password.getBytes("UTF-8")), "UTF-8");
            }
            KerberosKey key = new KerberosKey(principal, password.toCharArray(), null);
            IO.writeLine(key.toString());
        }
    }
}
