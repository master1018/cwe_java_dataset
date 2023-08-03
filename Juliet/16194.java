
package testcases.CWE319_Cleartext_Tx_Sensitive_Info;
import testcasesupport.*;
import java.io.*;
import java.net.PasswordAuthentication;
public class CWE319_Cleartext_Tx_Sensitive_Info__send_68a extends AbstractTestCase
{
    public static String data;
    public void bad() throws Throwable
    {
        PasswordAuthentication credentials = new PasswordAuthentication("user", "AP@ssw0rd".toCharArray());
        data = new String(credentials.getPassword());
        (new CWE319_Cleartext_Tx_Sensitive_Info__send_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        data = "Hello World";
        (new CWE319_Cleartext_Tx_Sensitive_Info__send_68b()).goodG2BSink();
    }
    private void goodB2G() throws Throwable
    {
        PasswordAuthentication credentials = new PasswordAuthentication("user", "AP@ssw0rd".toCharArray());
        data = new String(credentials.getPassword());
        (new CWE319_Cleartext_Tx_Sensitive_Info__send_68b()).goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
