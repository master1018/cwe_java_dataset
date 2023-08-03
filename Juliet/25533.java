
package testcases.CWE259_Hard_Coded_Password;
import testcasesupport.*;
import java.util.logging.Level;
import java.io.*;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.kerberos.KerberosKey;
public class CWE259_Hard_Coded_Password__kerberosKey_71b
{
    public void badSink(Object dataObject ) throws Throwable
    {
        String data = (String)dataObject;
        if (data != null)
        {
            KerberosPrincipal principal = new KerberosPrincipal("test");
            KerberosKey key = new KerberosKey(principal, data.toCharArray(), null);
            IO.writeLine(key.toString());
        }
    }
    public void goodG2BSink(Object dataObject ) throws Throwable
    {
        String data = (String)dataObject;
        if (data != null)
        {
            KerberosPrincipal principal = new KerberosPrincipal("test");
            KerberosKey key = new KerberosKey(principal, data.toCharArray(), null);
            IO.writeLine(key.toString());
        }
    }
}
