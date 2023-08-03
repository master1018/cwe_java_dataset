
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
public class CWE690_NULL_Deref_From_Return__Properties_getProperty_equals_66a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
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
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE690_NULL_Deref_From_Return__Properties_getProperty_equals_66b()).badSink(dataArray  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = "CWE690";
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE690_NULL_Deref_From_Return__Properties_getProperty_equals_66b()).goodG2BSink(dataArray  );
    }
    private void goodB2G() throws Throwable
    {
        String data;
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
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE690_NULL_Deref_From_Return__Properties_getProperty_equals_66b()).goodB2GSink(dataArray  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
