
package testcases.CWE259_Hard_Coded_Password;
import testcasesupport.*;
import java.util.logging.Level;
import java.io.*;
public class CWE259_Hard_Coded_Password__passwordAuth_66a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = "7e5tc4s3";
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE259_Hard_Coded_Password__passwordAuth_66b()).badSink(dataArray  );
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
        (new CWE259_Hard_Coded_Password__passwordAuth_66b()).goodG2BSink(dataArray  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
