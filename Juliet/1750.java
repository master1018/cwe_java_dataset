
package testcases.CWE129_Improper_Validation_of_Array_Index.s04;
import testcasesupport.*;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import javax.servlet.http.*;
import java.util.Properties;
import java.io.FileInputStream;
public class CWE129_Improper_Validation_of_Array_Index__PropertiesFile_array_read_check_min_75a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = Integer.MIN_VALUE; 
        {
            Properties properties = new Properties();
            FileInputStream streamFileInput = null;
            try
            {
                streamFileInput = new FileInputStream("../common/config.properties");
                properties.load(streamFileInput);
                String stringNumber = properties.getProperty("data");
                if (stringNumber != null) 
                {
                    try
                    {
                        data = Integer.parseInt(stringNumber.trim());
                    }
                    catch(NumberFormatException exceptNumberFormat)
                    {
                        IO.logger.log(Level.WARNING, "Number format exception parsing data from string", exceptNumberFormat);
                    }
                }
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
        ByteArrayOutputStream streamByteArrayOutput = null;
        ObjectOutput outputObject = null;
        try
        {
            streamByteArrayOutput = new ByteArrayOutputStream() ;
            outputObject = new ObjectOutputStream(streamByteArrayOutput) ;
            outputObject.writeObject(data);
            byte[] dataSerialized = streamByteArrayOutput.toByteArray();
            (new CWE129_Improper_Validation_of_Array_Index__PropertiesFile_array_read_check_min_75b()).badSink(dataSerialized  );
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "IOException in serialization", exceptIO);
        }
        finally
        {
            try
            {
                if (outputObject != null)
                {
                    outputObject.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing ObjectOutputStream", exceptIO);
            }
            try
            {
                if (streamByteArrayOutput != null)
                {
                    streamByteArrayOutput.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing ByteArrayOutputStream", exceptIO);
            }
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        ByteArrayOutputStream streamByteArrayOutput = null;
        ObjectOutput outputObject = null;
        try
        {
            streamByteArrayOutput = new ByteArrayOutputStream() ;
            outputObject = new ObjectOutputStream(streamByteArrayOutput) ;
            outputObject.writeObject(data);
            byte[] dataSerialized = streamByteArrayOutput.toByteArray();
            (new CWE129_Improper_Validation_of_Array_Index__PropertiesFile_array_read_check_min_75b()).goodG2BSink(dataSerialized  );
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "IOException in serialization", exceptIO);
        }
        finally
        {
            try
            {
                if (outputObject != null)
                {
                    outputObject.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing ObjectOutputStream", exceptIO);
            }
            try
            {
                if (streamByteArrayOutput != null)
                {
                    streamByteArrayOutput.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing ByteArrayOutputStream", exceptIO);
            }
        }
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = Integer.MIN_VALUE; 
        {
            Properties properties = new Properties();
            FileInputStream streamFileInput = null;
            try
            {
                streamFileInput = new FileInputStream("../common/config.properties");
                properties.load(streamFileInput);
                String stringNumber = properties.getProperty("data");
                if (stringNumber != null) 
                {
                    try
                    {
                        data = Integer.parseInt(stringNumber.trim());
                    }
                    catch(NumberFormatException exceptNumberFormat)
                    {
                        IO.logger.log(Level.WARNING, "Number format exception parsing data from string", exceptNumberFormat);
                    }
                }
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
        ByteArrayOutputStream streamByteArrayOutput = null;
        ObjectOutput outputObject = null;
        try
        {
            streamByteArrayOutput = new ByteArrayOutputStream() ;
            outputObject = new ObjectOutputStream(streamByteArrayOutput) ;
            outputObject.writeObject(data);
            byte[] dataSerialized = streamByteArrayOutput.toByteArray();
            (new CWE129_Improper_Validation_of_Array_Index__PropertiesFile_array_read_check_min_75b()).goodB2GSink(dataSerialized  );
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "IOException in serialization", exceptIO);
        }
        finally
        {
            try
            {
                if (outputObject != null)
                {
                    outputObject.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing ObjectOutputStream", exceptIO);
            }
            try
            {
                if (streamByteArrayOutput != null)
                {
                    streamByteArrayOutput.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing ByteArrayOutputStream", exceptIO);
            }
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}