
package testcases.CWE379_Temporary_File_Creation_in_Insecure_Dir;
import testcasesupport.*;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
public class CWE379_Temporary_File_Creation_in_Insecure_Dir__basic_14 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (IO.staticFive == 5)
        {
            FileOutputStream streamFileOutput = null;
            OutputStreamWriter writerOutputStream = null;
            BufferedWriter writerBuffered = null;
            File tempFile = null;
            String directoryName;
            if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
            {
                directoryName = "C:\\testcases\\insecureDir";
            }
            else
            {
                directoryName = "/home/user/testcases/insecureDir/";
            }
            try
            {
                File newDirectory = new File(directoryName);
                boolean isSuccessful = newDirectory.mkdir();
                if (isSuccessful)
                {
                    IO.writeLine("Directory created");
                    tempFile = File.createTempFile("temp", "1234", newDirectory);
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
    }
    private void good1() throws Throwable
    {
        if (IO.staticFive != 5)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            FileOutputStream streamFileOutput = null;
            OutputStreamWriter writerOutputStream = null;
            BufferedWriter writerBuffered = null;
            File tempFile = null;
            String directoryName;
            if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
            {
                directoryName = ".\\src\\testcases\\CWE379_File_Creation_in_Insecure_Dir\\secureDir";
            }
            else
            {
                directoryName = "/home/user/testcases/CWE379_File_Creation_in_Insecure_Dir/secureDir/";
            }
            try
            {
                File newDirectory = new File(directoryName);
                if (!newDirectory.setWritable(true, true))
                {
                    IO.logger.log(Level.WARNING, "Could not set Writable permissions");
                }
                if (!newDirectory.setReadable(true, true))
                {
                    IO.logger.log(Level.WARNING, "Could not set Readable permissions");
                }
                if (!newDirectory.setExecutable(false))
                {
                    IO.logger.log(Level.WARNING, "Could not set Executable permissions");
                }
                boolean isSuccessful = newDirectory.mkdir();
                if (isSuccessful)
                {
                    IO.writeLine("Directory created");
                    tempFile = File.createTempFile("temp", "1234", newDirectory);
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
    }
    private void good2() throws Throwable
    {
        if (IO.staticFive == 5)
        {
            FileOutputStream streamFileOutput = null;
            OutputStreamWriter writerOutputStream = null;
            BufferedWriter writerBuffered = null;
            File tempFile = null;
            String directoryName;
            if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
            {
                directoryName = ".\\src\\testcases\\CWE379_File_Creation_in_Insecure_Dir\\secureDir";
            }
            else
            {
                directoryName = "/home/user/testcases/CWE379_File_Creation_in_Insecure_Dir/secureDir/";
            }
            try
            {
                File newDirectory = new File(directoryName);
                if (!newDirectory.setWritable(true, true))
                {
                    IO.logger.log(Level.WARNING, "Could not set Writable permissions");
                }
                if (!newDirectory.setReadable(true, true))
                {
                    IO.logger.log(Level.WARNING, "Could not set Readable permissions");
                }
                if (!newDirectory.setExecutable(false))
                {
                    IO.logger.log(Level.WARNING, "Could not set Executable permissions");
                }
                boolean isSuccessful = newDirectory.mkdir();
                if (isSuccessful)
                {
                    IO.writeLine("Directory created");
                    tempFile = File.createTempFile("temp", "1234", newDirectory);
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
    }
    public void good() throws Throwable
    {
        good1();
        good2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
