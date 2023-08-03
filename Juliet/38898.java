
package testcases.CWE470_Unsafe_Reflection;
import testcasesupport.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.logging.Level;
public class CWE470_Unsafe_Reflection__console_readLine_22b
{
    public String badSource() throws Throwable
    {
        String data;
        if (CWE470_Unsafe_Reflection__console_readLine_22a.badPublicStatic)
        {
            data = ""; 
            {
                InputStreamReader readerInputStream = null;
                BufferedReader readerBuffered = null;
                try
                {
                    readerInputStream = new InputStreamReader(System.in, "UTF-8");
                    readerBuffered = new BufferedReader(readerInputStream);
                    data = readerBuffered.readLine();
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
        }
        else
        {
            data = null;
        }
        return data;
    }
    public String goodG2B1Source() throws Throwable
    {
        String data;
        if (CWE470_Unsafe_Reflection__console_readLine_22a.goodG2B1PublicStatic)
        {
            data = null;
        }
        else
        {
            data = "Testing.test";
        }
        return data;
    }
    public String goodG2B2Source() throws Throwable
    {
        String data;
        if (CWE470_Unsafe_Reflection__console_readLine_22a.goodG2B2PublicStatic)
        {
            data = "Testing.test";
        }
        else
        {
            data = null;
        }
        return data;
    }
}
