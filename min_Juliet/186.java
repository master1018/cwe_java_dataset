
package testcases.CWE111_Unsafe_JNI;
import testcasesupport.AbstractTestCaseBadOnly;
import testcasesupport.IO;
import java.io.*;
import java.util.logging.Level;
public class CWE111_Unsafe_JNI__console_01 extends AbstractTestCaseBadOnly 
{
    native String test(String s1, int len);
    static 
    {
        try
        {
            System.loadLibrary("JNITest"); 
        }
        catch (UnsatisfiedLinkError errorUnsatisfiedLink)
        {
            IO.logger.log(Level.WARNING, "Error, the library does not exist", errorUnsatisfiedLink);
        }
    }
    public void bad() throws IOException 
    {
        InputStreamReader readerInputStream = null;
        BufferedReader readerBuffered = null;
        int intNumber = 0;
        try
        {
            IO.writeLine("Enter a string: (asdf)" );
            readerInputStream = new InputStreamReader(System.in, "UTF-8");
            readerBuffered = new BufferedReader(readerInputStream);
            String stringLine = readerBuffered.readLine();
            IO.writeLine("How long was your string? (200) ");
            intNumber = Integer.parseInt(readerBuffered.readLine());
            IO.writeLine("Result from native method: " + test(stringLine, intNumber)); 
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "Error with stream reading", exceptIO);
            return;
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
    public static void main(String[] args) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
