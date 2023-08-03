
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import java.util.Vector;
import javax.servlet.http.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
public class CWE690_NULL_Deref_From_Return__Properties_getProperty_equals_72a extends AbstractTestCase
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
        Vector<String> dataVector = new Vector<String>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE690_NULL_Deref_From_Return__Properties_getProperty_equals_72b()).badSink(dataVector  );
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
        Vector<String> dataVector = new Vector<String>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE690_NULL_Deref_From_Return__Properties_getProperty_equals_72b()).goodG2BSink(dataVector  );
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
        Vector<String> dataVector = new Vector<String>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE690_NULL_Deref_From_Return__Properties_getProperty_equals_72b()).goodB2GSink(dataVector  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
