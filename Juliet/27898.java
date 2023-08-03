
package testcases.CWE256_Plaintext_Storage_of_Password;
import testcasesupport.*;
import java.util.HashMap;
import javax.servlet.http.*;
import java.util.logging.Level;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
public class CWE256_Plaintext_Storage_of_Password__basic_74a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String password;
        password = ""; 
        Properties properties = new Properties();
        FileInputStream streamFileInput = null;
        try
        {
            streamFileInput = new FileInputStream("../common/config.properties");
            properties.load(streamFileInput);
            password = properties.getProperty("password");
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
        HashMap<Integer,String> passwordHashMap = new HashMap<Integer,String>();
        passwordHashMap.put(0, password);
        passwordHashMap.put(1, password);
        passwordHashMap.put(2, password);
        (new CWE256_Plaintext_Storage_of_Password__basic_74b()).badSink(passwordHashMap  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        String password;
        password = ""; 
        Properties properties = new Properties();
        FileInputStream streamFileInput = null;
        try
        {
            streamFileInput = new FileInputStream("../common/config.properties");
            properties.load(streamFileInput);
            password = properties.getProperty("password");
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
        {
            Cipher aesCipher = Cipher.getInstance("AES");
            SecretKeySpec secretKeySpec = new SecretKeySpec("ABCDEFGHABCDEFGH".getBytes("UTF-8"), "AES");
            aesCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            String decryptedPassword = new String(aesCipher.doFinal(password.getBytes("UTF-8")), "UTF-8");
            password = decryptedPassword;
        }
        HashMap<Integer,String> passwordHashMap = new HashMap<Integer,String>();
        passwordHashMap.put(0, password);
        passwordHashMap.put(1, password);
        passwordHashMap.put(2, password);
        (new CWE256_Plaintext_Storage_of_Password__basic_74b()).goodG2BSink(passwordHashMap  );
    }
    private void goodB2G() throws Throwable
    {
        String password;
        password = ""; 
        Properties properties = new Properties();
        FileInputStream streamFileInput = null;
        try
        {
            streamFileInput = new FileInputStream("../common/config.properties");
            properties.load(streamFileInput);
            password = properties.getProperty("password");
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
        HashMap<Integer,String> passwordHashMap = new HashMap<Integer,String>();
        passwordHashMap.put(0, password);
        passwordHashMap.put(1, password);
        passwordHashMap.put(2, password);
        (new CWE256_Plaintext_Storage_of_Password__basic_74b()).goodB2GSink(passwordHashMap  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
