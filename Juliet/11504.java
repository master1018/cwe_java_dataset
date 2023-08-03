
package testcases.CWE526_Info_Exposure_Environment_Variables;
import testcasesupport.*;
public class CWE526_Info_Exposure_Environment_Variables__writeLine_03 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (5 == 5)
        {
            IO.writeLine("Not in path: " + System.getenv("PATH"));
        }
    }
    private void good1() throws Throwable
    {
        if (5 != 5)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            IO.writeLine("Not in path");
        }
    }
    private void good2() throws Throwable
    {
        if (5 == 5)
        {
            IO.writeLine("Not in path");
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
