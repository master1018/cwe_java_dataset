
package testcases.CWE378_Temporary_File_Creation_With_Insecure_Perms;
import testcasesupport.*;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
public class CWE378_Temporary_File_Creation_With_Insecure_Perms__basic_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        FileOutputStream streamFileOutput = null;
        OutputStreamWriter writerOutputStream = null;
        BufferedWriter writerBuffered = null;
        File tempFile = null;
        try
        {
            tempFile = File.createTempFile("temp", "1234");
            streamFileOutput = new FileOutputStream(tempFile);
            writerOutputStream = new OutputStreamWriter(streamFileOutput, "UTF-8");
            writerBuffered = new BufferedWriter(writerOutputStream);
            writerBuffered.write(42);
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "Error writing to temporary file", exceptIO);
        }
        finally
        {
            if (tempFile.exists())
            {
                tempFile.delete();
            }
            try
            {
                if (writerBuffered != null)
                {
                    writerBuffered.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing BufferedWriter", exceptIO);
            }
            try
            {
                if (writerOutputStream != null)
                {
                    writerOutputStream.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing OutputStreamWriter", exceptIO);
            }
            try
            {
                if (streamFileOutput != null)
                {
                    streamFileOutput.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing FileOutputStream", exceptIO);
            }
        }
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
    {
        FileOutputStream streamFileOutput = null;
        OutputStreamWriter writerOutputStream = null;
        BufferedWriter writerBuffered = null;
        File tempFile = null;
        try
        {
            tempFile = File.createTempFile("temp", "1234");
            if (!tempFile.setWritable(true, true))
            {
                IO.logger.log(Level.WARNING, "Could not set Writable permissions");
            }
            if (!tempFile.setReadable(true, true))
            {
                IO.logger.log(Level.WARNING, "Could not set Readable permissions");
            }
            if (!tempFile.setExecutable(false))
            {
                IO.logger.log(Level.WARNING, "Could not set Executable permissions");
            }
            streamFileOutput = new FileOutputStream(tempFile);
            writerOutputStream = new OutputStreamWriter(streamFileOutput, "UTF-8");
            writerBuffered = new BufferedWriter(writerOutputStream);
            writerBuffered.write(42);
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "Error writing to temporary file", exceptIO);
        }
        finally
        {
            if (tempFile.exists())
            {
                tempFile.delete();
            }
            try
            {
                if (writerBuffered != null)
                {
                    writerBuffered.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing BufferedWriter", exceptIO);
            }
            try
            {
                if (writerOutputStream != null)
                {
                    writerOutputStream.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing OutputStreamWriter", exceptIO);
            }
            try
            {
                if (streamFileOutput != null)
                {
                    streamFileOutput.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing FileOutputStream", exceptIO);
            }
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
