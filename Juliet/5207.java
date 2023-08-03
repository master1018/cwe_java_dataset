
package testcases.CWE319_Cleartext_Tx_Sensitive_Info;
import testcasesupport.*;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.kerberos.KerberosKey;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
public class CWE319_Cleartext_Tx_Sensitive_Info__listen_tcp_kerberosKey_81_bad extends CWE319_Cleartext_Tx_Sensitive_Info__listen_tcp_kerberosKey_81_base
{
    public void action(String password ) throws Throwable
    {
        if (password != null)
        {
            KerberosPrincipal principal = new KerberosPrincipal("test");
            KerberosKey key = new KerberosKey(principal, password.toCharArray(), null);
            IO.writeLine(key.toString());
        }
    }
}
