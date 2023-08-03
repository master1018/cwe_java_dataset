
package testcases.CWE319_Cleartext_Tx_Sensitive_Info;
import testcasesupport.*;
import java.io.*;
import java.net.PasswordAuthentication;
public class CWE319_Cleartext_Tx_Sensitive_Info__send_22a extends AbstractTestCase
{
    public static boolean badPublicStatic = false;
    public void bad() throws Throwable
    {
        String data = null;
        PasswordAuthentication credentials = new PasswordAuthentication("user", "AP@ssw0rd".toCharArray());
        data = new String(credentials.getPassword());
        badPublicStatic = true;
        (new CWE319_Cleartext_Tx_Sensitive_Info__send_22b()).badSink(data );
    }
    public static boolean goodB2G1PublicStatic = false;
    public static boolean goodB2G2PublicStatic = false;
    public static boolean goodG2BPublicStatic = false;
    public void good() throws Throwable
    {
        goodB2G1();
        goodB2G2();
        goodG2B();
    }
    private void goodB2G1() throws Throwable
    {
        String data = null;
        PasswordAuthentication credentials = new PasswordAuthentication("user", "AP@ssw0rd".toCharArray());
        data = new String(credentials.getPassword());
        goodB2G1PublicStatic = false;
        (new CWE319_Cleartext_Tx_Sensitive_Info__send_22b()).goodB2G1Sink(data );
    }
    private void goodB2G2() throws Throwable
    {
        String data = null;
        PasswordAuthentication credentials = new PasswordAuthentication("user", "AP@ssw0rd".toCharArray());
        data = new String(credentials.getPassword());
        goodB2G2PublicStatic = true;
        (new CWE319_Cleartext_Tx_Sensitive_Info__send_22b()).goodB2G2Sink(data );
    }
    private void goodG2B() throws Throwable
    {
        String data = null;
        data = "Hello World";
        goodG2BPublicStatic = true;
        (new CWE319_Cleartext_Tx_Sensitive_Info__send_22b()).goodG2BSink(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
