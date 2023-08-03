
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
public class CWE690_NULL_Deref_From_Return__Properties_getProperty_equals_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        if(IO.staticReturnsTrueOrFalse())
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
            data = "CWE690";
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
        }
        else
        {
            if("CWE690".equals(data))
            {
                IO.writeLine("data is CWE690");
            }
        }
    }
    private void goodG2B() throws Throwable
    {
        String data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = "CWE690";
        }
        else
        {
            data = "CWE690";
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
        }
        else
        {
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
        }
    }
    private void goodB2G() throws Throwable
    {
        String data;
        if(IO.staticReturnsTrueOrFalse())
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
        if(IO.staticReturnsTrueOrFalse())
        {
            if("CWE690".equals(data))
            {
                IO.writeLine("data is CWE690");
            }
        }
        else
        {
            if("CWE690".equals(data))
            {
                IO.writeLine("data is CWE690");
            }
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
