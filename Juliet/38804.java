
package testcases.CWE319_Cleartext_Tx_Sensitive_Info;
import testcasesupport.*;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.kerberos.KerberosKey;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
public class CWE319_Cleartext_Tx_Sensitive_Info__connect_tcp_kerberosKey_22b
{
    public void badSink(String password ) throws Throwable
    {
        if (CWE319_Cleartext_Tx_Sensitive_Info__connect_tcp_kerberosKey_22a.badPublicStatic)
        {
            if (password != null)
            {
                KerberosPrincipal principal = new KerberosPrincipal("test");
                KerberosKey key = new KerberosKey(principal, password.toCharArray(), null);
                IO.writeLine(key.toString());
            }
        }
        else
        {
            password = null;
        }
    }
    public void goodB2G1Sink(String password ) throws Throwable
    {
        if (CWE319_Cleartext_Tx_Sensitive_Info__connect_tcp_kerberosKey_22a.goodB2G1PublicStatic)
        {
            password = null;
        }
        else
        {
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
    public void goodB2G2Sink(String password ) throws Throwable
    {
        if (CWE319_Cleartext_Tx_Sensitive_Info__connect_tcp_kerberosKey_22a.goodB2G2PublicStatic)
        {
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
        else
        {
            password = null;
        }
    }
    public void goodG2BSink(String password ) throws Throwable
    {
        if (CWE319_Cleartext_Tx_Sensitive_Info__connect_tcp_kerberosKey_22a.goodG2BPublicStatic)
        {
            if (password != null)
            {
                KerberosPrincipal principal = new KerberosPrincipal("test");
                KerberosKey key = new KerberosKey(principal, password.toCharArray(), null);
                IO.writeLine(key.toString());
            }
        }
        else
        {
            password = null;
        }
    }
}
