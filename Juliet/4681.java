
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
public class CWE690_NULL_Deref_From_Return__Properties_getProperty_trim_05 extends AbstractTestCase
{
    private boolean privateTrue = true;
    private boolean privateFalse = false;
    public void bad() throws Throwable
    {
        String data;
        if (privateTrue)
        {
            FileInputStream streamFileInput = null;
            String propertiesFileName = "./CWE690_NULL_Deref_From_Return__Helper.properties";
            try
            {
                streamFileInput = new FileInputStream(propertiesFileName);
                Properties properties = new Properties();
                properties.load(streamFileInput);
                data = properties.getProperty("CWE690");
            }
            catch (IOException exceptIO)
            {
                IO.writeLine("Could not open properties file: " + propertiesFileName);
                data = ""; 
            }
            finally
            {
                try
                {
                    if (streamFileInput != null)
                    {
                        streamFileInput.close();
                    }
                }
                catch (IOException e)
                {
                    IO.logger.log(Level.WARNING, "Error closing FileInputStream", e);
                }
            }
        }
        else
        {
            data = null;
        }
        if (privateTrue)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
    private void goodG2B1() throws Throwable
    {
        String data;
        if (privateFalse)
        {
            data = null;
        }
        else
        {
            data = "CWE690";
        }
        if (privateTrue)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
    private void goodG2B2() throws Throwable
    {
        String data;
        if (privateTrue)
        {
            data = "CWE690";
        }
        else
        {
            data = null;
        }
        if (privateTrue)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
    private void goodB2G1() throws Throwable
    {
        String data;
        if (privateTrue)
        {
            FileInputStream streamFileInput = null;
            String propertiesFileName = "./CWE690_NULL_Deref_From_Return__Helper.properties";
            try
            {
                streamFileInput = new FileInputStream(propertiesFileName);
                Properties properties = new Properties();
                properties.load(streamFileInput);
                data = properties.getProperty("CWE690");
            }
            catch (IOException exceptIO)
            {
                IO.writeLine("Could not open properties file: " + propertiesFileName);
                data = ""; 
            }
            finally
            {
                try
                {
                    if (streamFileInput != null)
                    {
                        streamFileInput.close();
                    }
                }
                catch (IOException e)
                {
                    IO.logger.log(Level.WARNING, "Error closing FileInputStream", e);
                }
            }
        }
        else
        {
            data = null;
        }
        if (privateFalse)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if (data != null)
            {
                String stringTrimmed = data.trim();
                IO.writeLine(stringTrimmed);
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        String data;
        if (privateTrue)
        {
            FileInputStream streamFileInput = null;
            String propertiesFileName = "./CWE690_NULL_Deref_From_Return__Helper.properties";
            try
            {
                streamFileInput = new FileInputStream(propertiesFileName);
                Properties properties = new Properties();
                properties.load(streamFileInput);
                data = properties.getProperty("CWE690");
            }
            catch (IOException exceptIO)
            {
                IO.writeLine("Could not open properties file: " + propertiesFileName);
                data = ""; 
            }
            finally
            {
                try
                {
                    if (streamFileInput != null)
                    {
                        streamFileInput.close();
                    }
                }
                catch (IOException e)
                {
                    IO.logger.log(Level.WARNING, "Error closing FileInputStream", e);
                }
            }
        }
        else
        {
            data = null;
        }
        if (privateTrue)
        {
            if (data != null)
            {
                String stringTrimmed = data.trim();
                IO.writeLine(stringTrimmed);
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
