
package testcases.CWE319_Cleartext_Tx_Sensitive_Info;
import testcasesupport.*;
import java.util.LinkedList;
import java.io.*;
import java.net.PasswordAuthentication;
public class CWE319_Cleartext_Tx_Sensitive_Info__send_73a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        PasswordAuthentication credentials = new PasswordAuthentication("user", "AP@ssw0rd".toCharArray());
        data = new String(credentials.getPassword());
        LinkedList<String> dataLinkedList = new LinkedList<String>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE319_Cleartext_Tx_Sensitive_Info__send_73b()).badSink(dataLinkedList  );
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
        LinkedList<String> dataLinkedList = new LinkedList<String>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE319_Cleartext_Tx_Sensitive_Info__send_73b()).goodG2BSink(dataLinkedList  );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        PasswordAuthentication credentials = new PasswordAuthentication("user", "AP@ssw0rd".toCharArray());
        data = new String(credentials.getPassword());
        LinkedList<String> dataLinkedList = new LinkedList<String>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE319_Cleartext_Tx_Sensitive_Info__send_73b()).goodB2GSink(dataLinkedList  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
