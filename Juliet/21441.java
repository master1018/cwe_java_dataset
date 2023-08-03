
package testcases.CWE526_Info_Exposure_Environment_Variables;
import testcasesupport.*;
public class CWE526_Info_Exposure_Environment_Variables__writeLine_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        IO.writeLine("Not in path: " + System.getenv("PATH"));
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
    {
        IO.writeLine("Not in path");
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
