
package testcases.CWE526_Info_Exposure_Environment_Variables;
import testcasesupport.*;
public class CWE526_Info_Exposure_Environment_Variables__writeLine_17 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        for(int j = 0; j < 1; j++)
        {
            IO.writeLine("Not in path: " + System.getenv("PATH"));
        }
    }
    private void good1() throws Throwable
    {
        for(int k = 0; k < 1; k++)
        {
            IO.writeLine("Not in path");
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
