
package testcases.CWE259_Hard_Coded_Password;
import testcasesupport.*;
import java.util.logging.Level;
import java.io.*;
public class CWE259_Hard_Coded_Password__kerberosKey_22b
{
    public String badSource() throws Throwable
    {
        String data;
        if (CWE259_Hard_Coded_Password__kerberosKey_22a.badPublicStatic)
        {
            data = "7e5tc4s3";
        }
        else
        {
            data = null;
        }
        return data;
    }
    public String goodG2B1Source() throws Throwable
    {
        String data;
        if (CWE259_Hard_Coded_Password__kerberosKey_22a.goodG2B1PublicStatic)
        {
            data = null;
        }
        else
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
        }
        return data;
    }
    public String goodG2B2Source() throws Throwable
    {
        String data;
        if (CWE259_Hard_Coded_Password__kerberosKey_22a.goodG2B2PublicStatic)
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
        }
        else
        {
            data = null;
        }
        return data;
    }
}
