
package testcases.CWE114_Process_Control;
import testcasesupport.*;
public class CWE114_Process_Control__basic_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (IO.staticReturnsTrueOrFalse())
        {
            String libraryName = "test.dll";
            System.loadLibrary(libraryName);
        }
        else
        {
            String root;
            String libraryName = "test.dll";
            if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
            {
                root = "C:\\libs\\";
            }
            else
            {
                root = "/home/user/libs/";
            }
            System.load(root + libraryName);
        }
    }
    private void good1() throws Throwable
    {
        if (IO.staticReturnsTrueOrFalse())
        {
            String root;
            String libraryName = "test.dll";
            if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
            {
                root = "C:\\libs\\";
            }
            else
            {
                root = "/home/user/libs/";
            }
            System.load(root + libraryName);
        }
        else
        {
            String root;
            String libraryName = "test.dll";
            if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
            {
                root = "C:\\libs\\";
            }
            else
            {
                root = "/home/user/libs/";
            }
            System.load(root + libraryName);
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
