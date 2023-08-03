
package testcases.CWE253_Incorrect_Check_of_Function_Return_Value;
import testcasesupport.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.logging.Level;
public class CWE253_Incorrect_Check_of_Function_Return_Value__FileInputStream_15 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        switch (7)
        {
        case 7:
            FileInputStream fis = null;
            try
            {
                int bytesToRead = 1024;
                byte[] byteArray = new byte[bytesToRead];
                fis = new FileInputStream("c:\\file.txt");
                if (fis.read(byteArray) == 0)
                {
                    IO.writeLine("Error reading file.");
                }
                else
                {
                    IO.writeLine(new String(byteArray, "UTF-8"));
                }
            }
            catch (FileNotFoundException exceptFileNotFound)
            {
                IO.logger.log(Level.WARNING, "FileNotFoundException opening file", exceptFileNotFound);
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "IOException reading file", exceptIO);
            }
            finally
            {
                try
                {
                    if (fis != null)
                    {
                        fis.close();
                    }
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "IOException closing FileInputStream", exceptIO);
                }
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void good1() throws Throwable
    {
        switch (8)
        {
        case 7:
            IO.writeLine("Benign, fixed string");
            break;
        default:
            FileInputStream fis = null;
            try
            {
                int bytesToRead = 1024;
                byte[] byteArray = new byte[bytesToRead];
                fis = new FileInputStream("c:\\file.txt");
                int numberOfBytesRead = fis.read(byteArray);
                if (numberOfBytesRead == -1)
                {
                    IO.writeLine("The end of the file has been reached.");
                }
                else
                {
                    if (numberOfBytesRead < bytesToRead)
                    {
                        IO.writeLine("Could not read " + bytesToRead + " bytes.");
                    }
                    else
                    {
                        IO.writeLine(new String(byteArray, "UTF-8"));
                    }
                }
            }
            catch (FileNotFoundException exceptFileNotFound)
            {
                IO.logger.log(Level.WARNING, "FileNotFoundException opening file", exceptFileNotFound);
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "IOException reading file", exceptIO);
            }
            finally
            {
                try
                {
                    if (fis != null)
                    {
                        fis.close();
                    }
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "IOException closing FileInputStream", exceptIO);
                }
            }
            break;
        }
    }
    private void good2() throws Throwable
    {
        switch (7)
        {
        case 7:
            FileInputStream fis = null;
            try
            {
                int bytesToRead = 1024;
                byte[] byteArray = new byte[bytesToRead];
                fis = new FileInputStream("c:\\file.txt");
                int numberOfBytesRead = fis.read(byteArray);
                if (numberOfBytesRead == -1)
                {
                    IO.writeLine("The end of the file has been reached.");
                }
                else
                {
                    if (numberOfBytesRead < bytesToRead)
                    {
                        IO.writeLine("Could not read " + bytesToRead + " bytes.");
                    }
                    else
                    {
                        IO.writeLine(new String(byteArray, "UTF-8"));
                    }
                }
            }
            catch (FileNotFoundException exceptFileNotFound)
            {
                IO.logger.log(Level.WARNING, "FileNotFoundException opening file", exceptFileNotFound);
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "IOException reading file", exceptIO);
            }
            finally
            {
                try
                {
                    if (fis != null)
                    {
                        fis.close();
                    }
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "IOException closing FileInputStream", exceptIO);
                }
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
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
