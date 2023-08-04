
package testcases.CWE400_Resource_Exhaustion.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.logging.Level;
public class CWE400_Resource_Exhaustion__Servlet_16 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        while(true)
        {
            if (request.getContentType() == null || !request.getContentType().contains("multipart/form-data"))
            {
                return;
            }
            FileOutputStream streamFileOutput = null;
            InputStream streamInput = null;
            try
            {
                streamFileOutput = new FileOutputStream("output_bad.dat");
                streamInput = request.getInputStream();
                for (;;)
                {
                    byte[] inputBytes = new byte[1024];
                    int bytesRead = streamInput.read(inputBytes); 
                    if (bytesRead == -1)
                    {
                        break;
                    }
                    streamFileOutput.write(inputBytes);
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
                    if (streamInput != null)
                    {
                        streamInput.close();
                    }
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error closing InputStream", exceptIO);
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
            response.getWriter().write("Uploaded file!");
            break;
        }
    }
    private void good1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        while(true)
        {
            if (request.getContentType() == null || !request.getContentType().contains("multipart/form-data"))
            {
                return;
            }
            FileOutputStream streamFileOutput = null;
            InputStream streamInput = null;
            try
            {
                streamFileOutput = new FileOutputStream("output_good.dat");
                streamInput = request.getInputStream();
                final int MAXSIZE = 10485760;
                int bytesReadCount = 0;
                for (;;)
                {
                    if (bytesReadCount >= MAXSIZE)
                    {
                        response.getWriter().write("File exceeds MAXSIZE!");
                        break;
                    }
                    byte[] inputBytes = new byte[1024];
                    int bytesRead = streamInput.read(inputBytes);
                    if (bytesRead == -1)
                    {
                        break;
                    }
                    bytesReadCount += bytesRead;
                    streamFileOutput.write(inputBytes);
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
                    if (streamInput != null)
                    {
                        streamInput.close();
                    }
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error closing InputStream", exceptIO);
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
            response.getWriter().write("Uploaded file!");
            break;
        }
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        good1(request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}