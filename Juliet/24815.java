
package testcases.CWE321_Hard_Coded_Cryptographic_Key;
import testcasesupport.*;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
public class CWE321_Hard_Coded_Cryptographic_Key__basic_22b
{
    public String badSource() throws Throwable
    {
        String data;
        if (CWE321_Hard_Coded_Cryptographic_Key__basic_22a.badPublicStatic)
        {
            data = "23 ~j;asn!@#/>as";
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
        if (CWE321_Hard_Coded_Cryptographic_Key__basic_22a.goodG2B1PublicStatic)
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
        if (CWE321_Hard_Coded_Cryptographic_Key__basic_22a.goodG2B2PublicStatic)
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
