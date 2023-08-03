
package testcases.CWE319_Cleartext_Tx_Sensitive_Info;
import testcasesupport.*;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
public class CWE319_Cleartext_Tx_Sensitive_Info__URLConnection_kerberosKey_73a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String password;
        password = ""; 
        {
            URLConnection urlConnection = (new URL("http:
            BufferedReader readerBuffered = null;
            InputStreamReader readerInputStream = null;
            try
            {
                readerInputStream = new InputStreamReader(urlConnection.getInputStream(), "UTF-8");
                readerBuffered = new BufferedReader(readerInputStream);
                password = readerBuffered.readLine();
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error with stream reading", exceptIO);
            }
            finally
            {
                try
                {
                    if (readerBuffered != null)
                    {
                        readerBuffered.close();
                    }
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error closing BufferedReader", exceptIO);
                }
                try
                {
                    if (readerInputStream != null)
                    {
                        readerInputStream.close();
                    }
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error closing InputStreamReader", exceptIO);
                }
            }
        }
        LinkedList<String> passwordLinkedList = new LinkedList<String>();
        passwordLinkedList.add(0, password);
        passwordLinkedList.add(1, password);
        passwordLinkedList.add(2, password);
        (new CWE319_Cleartext_Tx_Sensitive_Info__URLConnection_kerberosKey_73b()).badSink(passwordLinkedList  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        String password;
        password = "Password1234!";
        LinkedList<String> passwordLinkedList = new LinkedList<String>();
        passwordLinkedList.add(0, password);
        passwordLinkedList.add(1, password);
        passwordLinkedList.add(2, password);
        (new CWE319_Cleartext_Tx_Sensitive_Info__URLConnection_kerberosKey_73b()).goodG2BSink(passwordLinkedList  );
    }
    private void goodB2G() throws Throwable
    {
        String password;
        password = ""; 
        {
            URLConnection urlConnection = (new URL("http:
            BufferedReader readerBuffered = null;
            InputStreamReader readerInputStream = null;
            try
            {
                readerInputStream = new InputStreamReader(urlConnection.getInputStream(), "UTF-8");
                readerBuffered = new BufferedReader(readerInputStream);
                password = readerBuffered.readLine();
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error with stream reading", exceptIO);
            }
            finally
            {
                try
                {
                    if (readerBuffered != null)
                    {
                        readerBuffered.close();
                    }
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error closing BufferedReader", exceptIO);
                }
                try
                {
                    if (readerInputStream != null)
                    {
                        readerInputStream.close();
                    }
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error closing InputStreamReader", exceptIO);
                }
            }
        }
        LinkedList<String> passwordLinkedList = new LinkedList<String>();
        passwordLinkedList.add(0, password);
        passwordLinkedList.add(1, password);
        passwordLinkedList.add(2, password);
        (new CWE319_Cleartext_Tx_Sensitive_Info__URLConnection_kerberosKey_73b()).goodB2GSink(passwordLinkedList  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
