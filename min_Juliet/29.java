
package testcases.CWE259_Hard_Coded_Password;
import testcasesupport.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.io.*;
import java.net.PasswordAuthentication;
public class CWE259_Hard_Coded_Password__passwordAuth_73b
{
    public void badSink(LinkedList<String> dataLinkedList ) throws Throwable
    {
        String data = dataLinkedList.remove(2);
        if (data != null)
        {
            PasswordAuthentication credentials = new PasswordAuthentication("user", data.toCharArray());
            IO.writeLine(credentials.toString());
        }
    }
    public void goodG2BSink(LinkedList<String> dataLinkedList ) throws Throwable
    {
        String data = dataLinkedList.remove(2);
        if (data != null)
        {
            PasswordAuthentication credentials = new PasswordAuthentication("user", data.toCharArray());
            IO.writeLine(credentials.toString());
        }
    }
}
