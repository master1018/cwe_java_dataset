
package testcases.CWE114_Process_Control;
import testcasesupport.*;
public class CWE114_Process_Control__basic_07 extends AbstractTestCase
{
    private int privateFive = 5;
    public void bad() throws Throwable
    {
        if (privateFive == 5)
        {
            String libraryName = "test.dll";
            System.loadLibrary(libraryName);
        }
    }
    private void good1() throws Throwable
    {
        if (privateFive != 5)
        {
            IO.writeLine("Benign, fixed string");
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
    private void good2() throws Throwable
    {
        if (privateFive == 5)
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
        good2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
