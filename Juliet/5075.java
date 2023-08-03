
package testcases.CWE319_Cleartext_Tx_Sensitive_Info;
import testcasesupport.*;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.SSLSocket;
public class CWE319_Cleartext_Tx_Sensitive_Info__send_81_bad extends CWE319_Cleartext_Tx_Sensitive_Info__send_81_base
{
    public void action(String data ) throws Throwable
    {
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
}
