
package testcases.CWE114_Process_Control;
import testcasesupport.*;
public class CWE114_Process_Control__basic_17 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        for(int j = 0; j < 1; j++)
        {
            String libraryName = "test.dll";
            System.loadLibrary(libraryName);
        }
    }
    private void good1() throws Throwable
    {
        for(int k = 0; k < 1; k++)
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
