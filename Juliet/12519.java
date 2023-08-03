
package testcases.CWE256_Plaintext_Storage_of_Password;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
public class CWE256_Plaintext_Storage_of_Password__basic_52b
{
    public void badSink(String password ) throws Throwable
    {
        (new CWE256_Plaintext_Storage_of_Password__basic_52c()).badSink(password );
    }
    public void goodG2BSink(String password ) throws Throwable
    {
        (new CWE256_Plaintext_Storage_of_Password__basic_52c()).goodG2BSink(password );
    }
    public void goodB2GSink(String password ) throws Throwable
    {
        (new CWE256_Plaintext_Storage_of_Password__basic_52c()).goodB2GSink(password );
    }
}
