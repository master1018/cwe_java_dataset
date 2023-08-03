
package testcases.CWE397_Throw_Generic;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.logging.Level;
public class CWE397_Throw_Generic__declare_Throwable_01 extends AbstractTestCase 
{
    public void bad() throws Throwable  
    {
        FileInputStream streamFileInput = new FileInputStream("filename.txt"); 
        IO.writeLine("File 'filename.txt' exists");
        try 
        {
            streamFileInput.close();
        } 
        catch (IOException exceptIO) 
        {
            IO.logger.log(Level.WARNING, "Error closing FileInputStream", exceptIO);
        }
    }
    private void good1() throws FileNotFoundException 
    {
        FileInputStream streamFileInput = new FileInputStream("filename.txt"); 
        IO.writeLine("File 'filename.txt' exists");
        try 
        {
            streamFileInput.close();
        } 
        catch (IOException exceptIO) 
        {
            IO.logger.log(Level.WARNING, "Error closing FileInputStream", exceptIO);
        }
    }
    public void good() throws FileNotFoundException
    {
        good1();
    }
    public static void main(String[] args) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
