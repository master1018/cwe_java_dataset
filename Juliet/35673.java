
package testcases.CWE259_Hard_Coded_Password;
import testcasesupport.*;
import java.util.logging.Level;
import java.io.*;
public class CWE259_Hard_Coded_Password__passwordAuth_68a extends AbstractTestCase
{
    public static String data;
    public void bad() throws Throwable
    {
        data = "7e5tc4s3";
        (new CWE259_Hard_Coded_Password__passwordAuth_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
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
        (new CWE259_Hard_Coded_Password__passwordAuth_68b()).goodG2BSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
