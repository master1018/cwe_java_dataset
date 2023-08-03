
package testcases.CWE400_Resource_Exhaustion.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
public class CWE400_Resource_Exhaustion__getParameter_Servlet_write_53d
{
    public void badSink(int count , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        File file = new File("badSink.txt");
        FileOutputStream streamFileOutput = new FileOutputStream(file);
        OutputStreamWriter writerOutputStream = new OutputStreamWriter(streamFileOutput, "UTF-8");
        BufferedWriter writerBuffered = new BufferedWriter(writerOutputStream);
        int i;
        for (i = 0; i < count; i++)
        {
            try
            {
                writerBuffered.write("Hello");
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error with stream writing", exceptIO);
            }
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
    public void goodG2BSink(int count , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        File file = new File("badSink.txt");
        FileOutputStream streamFileOutput = new FileOutputStream(file);
        OutputStreamWriter writerOutputStream = new OutputStreamWriter(streamFileOutput, "UTF-8");
        BufferedWriter writerBuffered = new BufferedWriter(writerOutputStream);
        int i;
        for (i = 0; i < count; i++)
        {
            try
            {
                writerBuffered.write("Hello");
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error with stream writing", exceptIO);
            }
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
    public void goodB2GSink(int count , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (count > 0 && count <= 20)
        {
            File file = new File("goodSink.txt");
            FileOutputStream streamFileOutput = new FileOutputStream(file);
            OutputStreamWriter writerOutputStream = new OutputStreamWriter(streamFileOutput, "UTF-8");
            BufferedWriter writerBuffered = new BufferedWriter(writerOutputStream);
            int i;
            for (i = 0; i < count; i++)
            {
                try
                {
                    writerBuffered.write("Hello");
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error with stream writing", exceptIO);
                }
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
