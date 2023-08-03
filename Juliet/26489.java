
package testcases.CWE259_Hard_Coded_Password;
import testcasesupport.*;
import java.util.Vector;
import java.util.logging.Level;
import java.io.*;
public class CWE259_Hard_Coded_Password__driverManager_72a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = "7e5tc4s3";
        Vector<String> dataVector = new Vector<String>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE259_Hard_Coded_Password__driverManager_72b()).badSink(dataVector  );
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
        Vector<String> dataVector = new Vector<String>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE259_Hard_Coded_Password__driverManager_72b()).goodG2BSink(dataVector  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
