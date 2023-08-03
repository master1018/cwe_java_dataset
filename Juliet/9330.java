
package testcases.CWE321_Hard_Coded_Cryptographic_Key;
import testcasesupport.*;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
public class CWE321_Hard_Coded_Cryptographic_Key__basic_66a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = "23 ~j;asn!@#/>as";
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE321_Hard_Coded_Cryptographic_Key__basic_66b()).badSink(dataArray  );
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = ""; 
        try
        {
            InputStreamReader readerInputStream = new InputStreamReader(System.in, "UTF-8");
            BufferedReader readerBuffered = new BufferedReader(readerInputStream);
            data = readerBuffered.readLine();
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "Error with stream reading", exceptIO);
        }
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE321_Hard_Coded_Cryptographic_Key__basic_66b()).goodG2BSink(dataArray  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
