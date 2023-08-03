
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.logging.Level;
public class CWE789_Uncontrolled_Mem_Alloc__listen_tcp_HashSet_22b
{
    public int badSource() throws Throwable
    {
        int data;
        if (CWE789_Uncontrolled_Mem_Alloc__listen_tcp_HashSet_22a.badPublicStatic)
        {
            data = Integer.MIN_VALUE; 
            {
                ServerSocket listener = null;
                Socket socket = null;
                BufferedReader readerBuffered = null;
                InputStreamReader readerInputStream = null;
                try
                {
                    listener = new ServerSocket(39543);
                    socket = listener.accept();
                    readerInputStream = new InputStreamReader(socket.getInputStream(), "UTF-8");
                    readerBuffered = new BufferedReader(readerInputStream);
                    String stringNumber = readerBuffered.readLine();
                    if (stringNumber != null) 
                    {
                        try
                        {
                            data = Integer.parseInt(stringNumber.trim());
                        }
                        catch(NumberFormatException exceptNumberFormat)
                        {
                            IO.logger.log(Level.WARNING, "Number format exception parsing data from string", exceptNumberFormat);
                        }
                    }
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
                    try
                    {
                        if (listener != null)
                        {
                            listener.close();
                        }
                    }
                    catch (IOException exceptIO)
                    {
                        IO.logger.log(Level.WARNING, "Error closing ServerSocket", exceptIO);
                    }
                }
            }
        }
        else
        {
            data = 0;
        }
        return data;
    }
    public int goodG2B1Source() throws Throwable
    {
        int data;
        if (CWE789_Uncontrolled_Mem_Alloc__listen_tcp_HashSet_22a.goodG2B1PublicStatic)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        return data;
    }
    public int goodG2B2Source() throws Throwable
    {
        int data;
        if (CWE789_Uncontrolled_Mem_Alloc__listen_tcp_HashSet_22a.goodG2B2PublicStatic)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        return data;
    }
}
