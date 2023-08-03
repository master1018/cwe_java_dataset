
package testcases.CWE319_Cleartext_Tx_Sensitive_Info;
import testcasesupport.*;
import java.io.*;
import java.net.PasswordAuthentication;
public class CWE319_Cleartext_Tx_Sensitive_Info__send_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        PasswordAuthentication credentials = new PasswordAuthentication("user", "AP@ssw0rd".toCharArray());
        data = new String(credentials.getPassword());
        CWE319_Cleartext_Tx_Sensitive_Info__send_81_base baseObject = new CWE319_Cleartext_Tx_Sensitive_Info__send_81_bad();
        baseObject.action(data );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = "Hello World";
        CWE319_Cleartext_Tx_Sensitive_Info__send_81_base baseObject = new CWE319_Cleartext_Tx_Sensitive_Info__send_81_goodG2B();
        baseObject.action(data );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        PasswordAuthentication credentials = new PasswordAuthentication("user", "AP@ssw0rd".toCharArray());
        data = new String(credentials.getPassword());
        CWE319_Cleartext_Tx_Sensitive_Info__send_81_base baseObject = new CWE319_Cleartext_Tx_Sensitive_Info__send_81_goodB2G();
        baseObject.action(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
