
package testcases.CWE526_Info_Exposure_Environment_Variables;
import testcasesupport.*;
public class CWE526_Info_Exposure_Environment_Variables__writeLine_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        while(true)
        {
            IO.writeLine("Not in path: " + System.getenv("PATH"));
            break;
        }
    }
    private void good1() throws Throwable
    {
        while(true)
        {
            IO.writeLine("Not in path");
            break;
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
