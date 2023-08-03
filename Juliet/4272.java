
package testcases.CWE477_Obsolete_Functions;
import testcasesupport.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataInputStream;
public class CWE477_Obsolete_Functions__DataInputStream_readLine_15 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        switch (7)
        {
        case 7:
        {
            DataInputStream streamDataInput = new DataInputStream(System.in);
            String myString = streamDataInput.readLine();
            IO.writeLine(myString); 
        }
        break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void good1() throws Throwable
    {
        switch (8)
        {
        case 7:
            IO.writeLine("Benign, fixed string");
            break;
        default:
        {
            InputStreamReader readerInputStream = new InputStreamReader(System.in, "UTF-8");
            BufferedReader readerBuffered = new BufferedReader(readerInputStream);
            String myString = readerBuffered.readLine();
            IO.writeLine(myString); 
        }
        break;
        }
    }
    private void good2() throws Throwable
    {
        switch (7)
        {
        case 7:
        {
            InputStreamReader readerInputStream = new InputStreamReader(System.in, "UTF-8");
            BufferedReader readerBuffered = new BufferedReader(readerInputStream);
            String myString = readerBuffered.readLine();
            IO.writeLine(myString); 
        }
        break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    public void good() throws Throwable
    {
        good1();
        good2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
