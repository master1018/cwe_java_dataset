
package testcases.CWE506_Embedded_Malicious_Code;
import testcasesupport.*;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
public class CWE506_Embedded_Malicious_Code__file_properties_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        while(true)
        {
            FileOutputStream streamFileOutput = null;
            try
            {
                String path = "C:\\test_bad.txt";
                File file = new File(path);
                long lastModified = file.lastModified();
                streamFileOutput = new FileOutputStream(file);
                streamFileOutput.write("This is a new line".getBytes("UTF-8"));
                file.setLastModified(lastModified - 10000L);
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "File I/O error", exceptIO);
            }
            finally
            {
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
            break;
        }
    }
    private void good1() throws Throwable
    {
        while(true)
        {
            FileOutputStream streamFileOutput = null;
            try
            {
                String path = "C:\\test_good.txt";
                File file = new File(path);
                streamFileOutput = new FileOutputStream(file);
                streamFileOutput.write("This is a new line".getBytes("UTF-8"));
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "File I/O error", exceptIO);
            }
            finally
            {
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
            break;
        }
    }
    public void good() throws Throwable
    {
        good1();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
