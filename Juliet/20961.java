
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.logging.Level;
public class CWE190_Integer_Overflow__long_console_readLine_square_05 extends AbstractTestCase
{
    private boolean privateTrue = true;
    private boolean privateFalse = false;
    public void bad() throws Throwable
    {
        long data;
        if (privateTrue)
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
                    data = Long.parseLong(stringNumber.trim());
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
            data = 0L;
        }
        if (privateTrue)
        {
            long result = (long)(data * data);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B1() throws Throwable
    {
        long data;
        if (privateFalse)
        {
            data = 0L;
        }
        else
        {
            data = 2;
        }
        if (privateTrue)
        {
            long result = (long)(data * data);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B2() throws Throwable
    {
        long data;
        if (privateTrue)
        {
            data = 2;
        }
        else
        {
            data = 0L;
        }
        if (privateTrue)
        {
            long result = (long)(data * data);
            IO.writeLine("result: " + result);
        }
    }
    private void goodB2G1() throws Throwable
    {
        long data;
        if (privateTrue)
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
                    data = Long.parseLong(stringNumber.trim());
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
            data = 0L;
        }
        if (privateFalse)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if ((data != Integer.MIN_VALUE) && (data != Long.MIN_VALUE) && (Math.abs(data) <= (long)Math.sqrt(Long.MAX_VALUE)))
            {
                long result = (long)(data * data);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform squaring.");
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        long data;
        if (privateTrue)
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
                    data = Long.parseLong(stringNumber.trim());
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
            data = 0L;
        }
        if (privateTrue)
        {
            if ((data != Integer.MIN_VALUE) && (data != Long.MIN_VALUE) && (Math.abs(data) <= (long)Math.sqrt(Long.MAX_VALUE)))
            {
                long result = (long)(data * data);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform squaring.");
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
