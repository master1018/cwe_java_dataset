
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.logging.Level;
public class CWE190_Integer_Overflow__byte_console_readLine_add_07 extends AbstractTestCase
{
    private int privateFive = 5;
    public void bad() throws Throwable
    {
        byte data;
        if (privateFive==5)
        {
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
        }
        else
        {
            data = 0;
        }
        if (privateFive==5)
        {
            byte result = (byte)(data + 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B1() throws Throwable
    {
        byte data;
        if (privateFive!=5)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        if (privateFive==5)
        {
            byte result = (byte)(data + 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B2() throws Throwable
    {
        byte data;
        if (privateFive==5)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        if (privateFive==5)
        {
            byte result = (byte)(data + 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodB2G1() throws Throwable
    {
        byte data;
        if (privateFive==5)
        {
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
        }
        else
        {
            data = 0;
        }
        if (privateFive!=5)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if (data < Byte.MAX_VALUE)
            {
                byte result = (byte)(data + 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform addition.");
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        byte data;
        if (privateFive==5)
        {
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
        }
        else
        {
            data = 0;
        }
        if (privateFive==5)
        {
            if (data < Byte.MAX_VALUE)
            {
                byte result = (byte)(data + 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform addition.");
            }
        }
    }
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
        goodB2G1();
        goodB2G2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}