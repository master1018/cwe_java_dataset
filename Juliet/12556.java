
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.logging.Level;
public class CWE191_Integer_Underflow__byte_console_readLine_multiply_42 extends AbstractTestCase
{
    private byte badSource() throws Throwable
    {
        byte data;
        data = -1;
        BufferedReader readerBuffered = null;
        InputStreamReader readerInputStream = null;
        try
        {
            readerInputStream = new InputStreamReader(System.in, "UTF-8");
            readerBuffered = new BufferedReader(readerInputStream);
            String stringNumber = readerBuffered.readLine();
            if (stringNumber != null)
            {
                data = Byte.parseByte(stringNumber.trim());
            }
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "Error with stream reading", exceptIO);
        }
        catch (NumberFormatException exceptNumberFormat)
        {
            IO.logger.log(Level.WARNING, "Error with number parsing", exceptNumberFormat);
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
            finally
            {
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
        return data;
    }
    public void bad() throws Throwable
    {
        byte data = badSource();
        if(data < 0) 
        {
            byte result = (byte)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    private byte goodG2BSource() throws Throwable
    {
        byte data;
        data = 2;
        return data;
    }
    private void goodG2B() throws Throwable
    {
        byte data = goodG2BSource();
        if(data < 0) 
        {
            byte result = (byte)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    private byte goodB2GSource() throws Throwable
    {
        byte data;
        data = -1;
        BufferedReader readerBuffered = null;
        InputStreamReader readerInputStream = null;
        try
        {
            readerInputStream = new InputStreamReader(System.in, "UTF-8");
            readerBuffered = new BufferedReader(readerInputStream);
            String stringNumber = readerBuffered.readLine();
            if (stringNumber != null)
            {
                data = Byte.parseByte(stringNumber.trim());
            }
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "Error with stream reading", exceptIO);
        }
        catch (NumberFormatException exceptNumberFormat)
        {
            IO.logger.log(Level.WARNING, "Error with number parsing", exceptNumberFormat);
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
            finally
            {
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
        return data;
    }
    private void goodB2G() throws Throwable
    {
        byte data = goodB2GSource();
        if(data < 0) 
        {
            if (data > (Byte.MIN_VALUE/2))
            {
                byte result = (byte)(data * 2);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform multiplication.");
            }
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
