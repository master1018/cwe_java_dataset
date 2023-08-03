
package testcases.CWE470_Unsafe_Reflection;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
public class CWE470_Unsafe_Reflection__PropertiesFile_22b
{
    public String badSource() throws Throwable
    {
        String data;
        if (CWE470_Unsafe_Reflection__PropertiesFile_22a.badPublicStatic)
        {
            data = ""; 
            {
                Properties properties = new Properties();
                FileInputStream streamFileInput = null;
                try
                {
                    streamFileInput = new FileInputStream("../common/config.properties");
                    properties.load(streamFileInput);
                    data = properties.getProperty("data");
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error with stream reading", exceptIO);
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
                    catch (IOException exceptIO)
                    {
                        IO.logger.log(Level.WARNING, "Error closing FileInputStream", exceptIO);
                    }
                }
            }
        }
        else
        {
            data = null;
        }
        return data;
    }
    public String goodG2B1Source() throws Throwable
    {
        String data;
        if (CWE470_Unsafe_Reflection__PropertiesFile_22a.goodG2B1PublicStatic)
        {
            data = null;
        }
        else
        {
            data = "Testing.test";
        }
        return data;
    }
    public String goodG2B2Source() throws Throwable
    {
        String data;
        if (CWE470_Unsafe_Reflection__PropertiesFile_22a.goodG2B2PublicStatic)
        {
            data = "Testing.test";
        }
        else
        {
            data = null;
        }
        return data;
    }
}
