
package testcases.CWE114_Process_Control;
import testcasesupport.*;
public class CWE114_Process_Control__basic_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String libraryName = "test.dll";
        System.loadLibrary(libraryName);
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
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
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
