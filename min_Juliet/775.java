
package testcases.CWE775_Missing_Release_of_File_Descriptor_or_Handle;
import java.util.zip.ZipFile;
import java.util.zip.ZipException;
import java.io.IOException;
import java.util.logging.Level;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
public class CWE775_Missing_Release_of_File_Descriptor_or_Handle__ZipFile_01 extends AbstractTestCase 
{    
    public void bad() 
    {
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
    }
    private void good1() 
    {
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
