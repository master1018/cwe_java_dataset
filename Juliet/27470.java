
package testcases.CWE571_Expression_Always_True;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.logging.Level;
public class CWE571_Expression_Always_True__string_equals_01 extends AbstractTestCase 
{
    public void bad()
    {
        String stringTrue = "true";
        if (stringTrue.equals("true"))
        {
            IO.writeLine("always prints");
        }
    }
    public void good()
    {
        good1();
    }
    private void good1()
    {
        String stringInput = "";
        IO.writeLine("Enter a string: ");
        boolean isError = false;
        BufferedReader readerBuffered = null;
        InputStreamReader readerInputStream = null;
        try
        {
            readerInputStream = new InputStreamReader(System.in, "UTF-8");
            readerBuffered = new BufferedReader(readerInputStream);
            stringInput = readerBuffered.readLine();
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "Error with stream reading", exceptIO);
        }
        finally 
        {
            try 
            {
                if (readerBuffered != null) 
                {
                    readerBuffered.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing BufferedReader", exceptIO);
                isError = true;
            }
            try 
            {
                if (readerInputStream != null) 
                {   
                    readerInputStream.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing InputStreamReader", exceptIO);
                isError = true;
            }
        }
        if (isError) 
        { 
            return; 
        }
        if (stringInput.contentEquals("true"))
        {
            IO.writeLine("sometimes prints");
        }
    }
    public static void main(String[] args) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
