
package testcases.CWE597_Wrong_Operator_String_Comparison;
import testcasesupport.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.logging.Level;
public class CWE597_Wrong_Operator_String_Comparison__basic_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        BufferedReader readerBuffered = null;
        InputStreamReader readerInputStream = null;
        try
        {
            readerInputStream = new InputStreamReader(System.in, "UTF-8");
            readerBuffered = new BufferedReader(readerInputStream);
            IO.writeLine("Enter string1: "); 
            String string1 = readerBuffered.readLine();
            IO.writeLine("Enter string2: "); 
            String string2 = readerBuffered.readLine();
            if (string1 != null && string2 != null)
            {
                if (string1 == string2)     
                {
                    IO.writeLine("bad(): Strings are equal");
                }
                else
                {
                    IO.writeLine("bad(): Strings are not equal"); 
                }
            }
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "Error!", exceptIO);
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
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
    {
        BufferedReader readerBuffered = null;
        InputStreamReader readerInputStream = null;
        try
        {
            readerInputStream = new InputStreamReader(System.in, "UTF-8");
            readerBuffered = new BufferedReader(readerInputStream);
            IO.writeLine("Enter string1: "); 
            String string1 = readerBuffered.readLine();
            IO.writeLine("Enter string2: "); 
            String string2 = readerBuffered.readLine();
            if (string1 != null && string2 != null)
            {
                if (string1.equals(string2))  
                {
                    IO.writeLine("good(): Strings are equal");
                }
                else
                {
                    IO.writeLine("good(): Strings are not equal");
                }
            }
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "Error!", exceptIO);
        }
        finally
        {
            try
            {
                if (readerBuffered != null )
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
                if (readerInputStream != null )
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
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
