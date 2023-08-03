
package testcases.CWE259_Hard_Coded_Password;
import testcasesupport.*;
import java.util.logging.Level;
import java.io.*;
import java.net.PasswordAuthentication;
public class CWE259_Hard_Coded_Password__passwordAuth_22a extends AbstractTestCase
{
    public static boolean badPublicStatic = false;
    public void bad() throws Throwable
    {
        String data;
        badPublicStatic = true;
        data = (new CWE259_Hard_Coded_Password__passwordAuth_22b()).badSource();
        if (data != null)
        {
            PasswordAuthentication credentials = new PasswordAuthentication("user", data.toCharArray());
            IO.writeLine(credentials.toString());
        }
    }
    public static boolean goodG2B1PublicStatic = false;
    public static boolean goodG2B2PublicStatic = false;
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
    }
    private void goodG2B1() throws Throwable
    {
        String data;
        goodG2B1PublicStatic = false;
        data = (new CWE259_Hard_Coded_Password__passwordAuth_22b()).goodG2B1Source();
        if (data != null)
        {
            PasswordAuthentication credentials = new PasswordAuthentication("user", data.toCharArray());
            IO.writeLine(credentials.toString());
        }
    }
    private void goodG2B2() throws Throwable
    {
        String data;
        goodG2B2PublicStatic = true;
        data = (new CWE259_Hard_Coded_Password__passwordAuth_22b()).goodG2B2Source();
        if (data != null)
        {
            PasswordAuthentication credentials = new PasswordAuthentication("user", data.toCharArray());
            IO.writeLine(credentials.toString());
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
