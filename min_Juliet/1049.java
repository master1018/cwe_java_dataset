
package testcases.CWE390_Error_Without_Action;
import testcasesupport.*;
import java.io.File;
public class CWE390_Error_Without_Action__mkdirs_17 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        for(int j = 0; j < 1; j++)
        {
            File newDirectory = null;
            if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
            {
                newDirectory = new File("C:\\lvl_1\\lvl_2\\lvl_3\\");
            }
            else
            {
                newDirectory = new File("/home/user/lvl_1/lvl_2/lvl_3/");
            }
            if (!newDirectory.mkdirs())
            {
            }
        }
    }
    private void good1() throws Throwable
    {
        for(int k = 0; k < 1; k++)
        {
            File newDirectory = null;
            if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
            {
                newDirectory = new File("C:\\lvl_1\\lvl_2\\lvl_3\\");
            }
            else
            {
                newDirectory = new File("/home/user/lvl_1/lvl_2/lvl_3/");
            }
            if (!newDirectory.mkdirs())
            {
                IO.writeLine("The directories could not be created");
                StringBuilder errorString = new StringBuilder();
                errorString.append("The directories (");
                errorString.append(newDirectory.getAbsolutePath());
                errorString.append(") could not be created: ");
                throw new Exception(errorString.toString());
            }
        }
    }
    public void good() throws Throwable
    {
        good1();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}