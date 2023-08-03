
package testcases.CWE319_Cleartext_Tx_Sensitive_Info;
import testcasesupport.*;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.SSLSocket;
public class CWE319_Cleartext_Tx_Sensitive_Info__send_61a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data = (new CWE319_Cleartext_Tx_Sensitive_Info__send_61b()).badSource();
        Socket socket = null;
        PrintWriter writer = null;
        try
        {
            socket = new Socket("remote_host", 1337);
            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(data);
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "Error writing to the socket", exceptIO);
        }
        finally
        {
            if (writer != null)
            {
                writer.close();
            }
            try
            {
                if (socket != null)
                {
                    socket.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing Socket", exceptIO);
            }
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        String data = (new CWE319_Cleartext_Tx_Sensitive_Info__send_61b()).goodG2BSource();
        Socket socket = null;
        PrintWriter writer = null;
        try
        {
            socket = new Socket("remote_host", 1337);
            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(data);
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "Error writing to the socket", exceptIO);
        }
        finally
        {
            if (writer != null)
            {
                writer.close();
            }
            try
            {
                if (socket != null)
                {
                    socket.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing Socket", exceptIO);
            }
        }
    }
    private void goodB2G() throws Throwable
    {
        String data = (new CWE319_Cleartext_Tx_Sensitive_Info__send_61b()).goodB2GSource();
        SSLSocketFactory sslsSocketFactory = null;
        SSLSocket sslSocket = null;
        PrintWriter writer = null;
        try
        {
            sslsSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            sslSocket = (SSLSocket) sslsSocketFactory.createSocket("remote_host", 1337);
            writer = new PrintWriter(sslSocket.getOutputStream(), true);
            writer.println(data);
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "Error writing to the socket", exceptIO);
        }
        finally
        {
            if (writer != null)
            {
                writer.close();
            }
            try
            {
                if (sslSocket != null)
                {
                    sslSocket.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing SSLSocket", exceptIO);
            }
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
