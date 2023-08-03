
package testcases.CWE789_Uncontrolled_Mem_Alloc.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.HashMap;
public class CWE789_Uncontrolled_Mem_Alloc__URLConnection_HashMap_21 extends AbstractTestCase
{
    private boolean badPrivate = false;
    public void bad() throws Throwable
    {
        int data;
        badPrivate = true;
        data = bad_source();
        HashMap intHashMap = new HashMap(data);
    }
    private int bad_source() throws Throwable
    {
        int data;
        if (badPrivate)
        {
            data = Integer.MIN_VALUE; 
            {
                URLConnection urlConnection = (new URL("http:
                BufferedReader readerBuffered = null;
                InputStreamReader readerInputStream = null;
                try
                {
                    readerInputStream = new InputStreamReader(urlConnection.getInputStream(), "UTF-8");
                    readerBuffered = new BufferedReader(readerInputStream);
                    String stringNumber = readerBuffered.readLine();
                    if (stringNumber != null) 
                    {
                        try
                        {
                            data = Integer.parseInt(stringNumber.trim());
                        }
                        catch (NumberFormatException exceptNumberFormat)
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
                }
            }
        }
        else
        {
            data = 0;
        }
        return data;
    }
    private boolean goodG2B1_private = false;
    private boolean goodG2B2_private = false;
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        goodG2B1_private = false;
        data = goodG2B1_source();
        HashMap intHashMap = new HashMap(data);
    }
    private int goodG2B1_source() throws Throwable
    {
        int data = 0;
        if (goodG2B1_private)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        return data;
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        goodG2B2_private = true;
        data = goodG2B2_source();
        HashMap intHashMap = new HashMap(data);
    }
    private int goodG2B2_source() throws Throwable
    {
        int data = 0;
        if (goodG2B2_private)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        return data;
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
