
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import java.util.LinkedList;
import javax.servlet.http.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
public class CWE690_NULL_Deref_From_Return__Properties_getProperty_equals_73a extends AbstractTestCase
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
        LinkedList<String> dataLinkedList = new LinkedList<String>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE690_NULL_Deref_From_Return__Properties_getProperty_equals_73b()).badSink(dataLinkedList  );
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
        LinkedList<String> dataLinkedList = new LinkedList<String>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE690_NULL_Deref_From_Return__Properties_getProperty_equals_73b()).goodG2BSink(dataLinkedList  );
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
        LinkedList<String> dataLinkedList = new LinkedList<String>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE690_NULL_Deref_From_Return__Properties_getProperty_equals_73b()).goodB2GSink(dataLinkedList  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
