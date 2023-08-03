
package testcases.CWE319_Cleartext_Tx_Sensitive_Info;
import testcasesupport.*;
import java.io.*;
import java.net.PasswordAuthentication;
public class CWE319_Cleartext_Tx_Sensitive_Info__send_67a extends AbstractTestCase
{
    static class Container
    {
        public String containerOne;
    }
    public void bad() throws Throwable
    {
        String data;
        PasswordAuthentication credentials = new PasswordAuthentication("user", "AP@ssw0rd".toCharArray());
        data = new String(credentials.getPassword());
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE319_Cleartext_Tx_Sensitive_Info__send_67b()).badSink(dataContainer  );
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
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE319_Cleartext_Tx_Sensitive_Info__send_67b()).goodG2BSink(dataContainer  );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        PasswordAuthentication credentials = new PasswordAuthentication("user", "AP@ssw0rd".toCharArray());
        data = new String(credentials.getPassword());
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE319_Cleartext_Tx_Sensitive_Info__send_67b()).goodB2GSink(dataContainer  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
