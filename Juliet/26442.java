
package testcases.CWE259_Hard_Coded_Password;
import testcasesupport.*;
import java.util.logging.Level;
import java.io.*;
import java.net.PasswordAuthentication;
public class CWE259_Hard_Coded_Password__passwordAuth_67b
{
    public void badSink(CWE259_Hard_Coded_Password__passwordAuth_67a.Container dataContainer ) throws Throwable
    {
        String data = dataContainer.containerOne;
        if (data != null)
        {
            PasswordAuthentication credentials = new PasswordAuthentication("user", data.toCharArray());
            IO.writeLine(credentials.toString());
        }
    }
    public void goodG2BSink(CWE259_Hard_Coded_Password__passwordAuth_67a.Container dataContainer ) throws Throwable
    {
        String data = dataContainer.containerOne;
        if (data != null)
        {
            PasswordAuthentication credentials = new PasswordAuthentication("user", data.toCharArray());
            IO.writeLine(credentials.toString());
        }
    }
}
