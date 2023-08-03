
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
public class CWE690_NULL_Deref_From_Return__Properties_getProperty_equals_15 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        switch (6)
        {
        case 6:
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
            break;
        default:
            data = null;
            break;
        }
        switch (7)
        {
        case 7:
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodG2B1() throws Throwable
    {
        String data;
        switch (5)
        {
        case 6:
            data = null;
            break;
        default:
            data = "CWE690";
            break;
        }
        switch (7)
        {
        case 7:
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodG2B2() throws Throwable
    {
        String data;
        switch (6)
        {
        case 6:
            data = "CWE690";
            break;
        default:
            data = null;
            break;
        }
        switch (7)
        {
        case 7:
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodB2G1() throws Throwable
    {
        String data;
        switch (6)
        {
        case 6:
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
            break;
        default:
            data = null;
            break;
        }
        switch (8)
        {
        case 7:
            IO.writeLine("Benign, fixed string");
            break;
        default:
            if("CWE690".equals(data))
            {
                IO.writeLine("data is CWE690");
            }
            break;
        }
    }
    private void goodB2G2() throws Throwable
    {
        String data;
        switch (6)
        {
        case 6:
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
            break;
        default:
            data = null;
            break;
        }
        switch (7)
        {
        case 7:
            if("CWE690".equals(data))
            {
                IO.writeLine("data is CWE690");
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
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
