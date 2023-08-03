
package testcases.CWE477_Obsolete_Functions;
import testcasesupport.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataInputStream;
public class CWE477_Obsolete_Functions__DataInputStream_readLine_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        {
            DataInputStream streamDataInput = new DataInputStream(System.in);
            String myString = streamDataInput.readLine();
            IO.writeLine(myString); 
        }
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
    {
        {
            InputStreamReader readerInputStream = new InputStreamReader(System.in, "UTF-8");
            BufferedReader readerBuffered = new BufferedReader(readerInputStream);
            String myString = readerBuffered.readLine();
            IO.writeLine(myString); 
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
