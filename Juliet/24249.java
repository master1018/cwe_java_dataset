
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.HashMap;
public class CWE789_Uncontrolled_Mem_Alloc__PropertiesFile_HashMap_14 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        if (IO.staticFive == 5)
        {
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
        }
        else
        {
            data = 0;
        }
        HashMap intHashMap = new HashMap(data);
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        if (IO.staticFive != 5)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        HashMap intHashMap = new HashMap(data);
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        if (IO.staticFive == 5)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        HashMap intHashMap = new HashMap(data);
    }
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
