
package testcases.CWE404_Improper_Resource_Shutdown;
import java.util.zip.ZipFile;
import java.util.zip.ZipException;
import java.io.IOException;
import java.util.logging.Level;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
public class CWE404_Improper_Resource_Shutdown__ZipFile_01 extends AbstractTestCase 
{    
    public void bad() 
    {
        String filename = null;
        if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
        {
            filename = "C:\\file.zip";
        }
        else
        {
            filename = "/home/user/file.zip";
        }
        ZipFile zFile = null;
        try
        {
            zFile = new ZipFile(filename);          
            IO.writeLine("File contains " + zFile.size() + " entries.");
            try 
            {
                if (zFile != null)
                {
                    zFile.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing ZipFile", exceptIO);
            }        
        }
        catch (ZipException exceptZip)
        {
            IO.logger.log(Level.WARNING, "Error with ZIP format", exceptZip);
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "Error reading file", exceptIO);
        }
    }
    private void good1() 
    {
        String filename = null;
        if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
        {
            filename = "C:\\file.zip";
        }
        else
        {
            filename = "/home/user/file.zip";
        }
        ZipFile zFile = null;
        try
        {
            zFile = new ZipFile("C:\\file.zip");          
            IO.writeLine("File contains " + zFile.size() + " entries.");                  
        }
        catch (ZipException exceptZip)
        {
            IO.logger.log(Level.WARNING, "Error with ZIP format", exceptZip);
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "Error reading file", exceptIO);
        }
        finally
        {
            try 
            {
                if (zFile != null)
                {
                    zFile.close();
                }
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error closing ZipFile", exceptIO);
            }  
        }
    }
    public void good()  
    {
        good1();
    }
    public static void main(String[] args) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
