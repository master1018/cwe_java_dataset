
package testcases.CWE486_Compare_Classes_by_Name;
import testcasesupport.*;
public class CWE486_Compare_Classes_by_Name__basic_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        testcases.CWE486_Compare_Classes_by_Name.HelperClass.CWE486_Compare_Classes_by_Name__Helper helperClass = new testcases.CWE486_Compare_Classes_by_Name.HelperClass.CWE486_Compare_Classes_by_Name__Helper();
        testcases.CWE486_Compare_Classes_by_Name.CWE486_Compare_Classes_by_Name__Helper helperClassRoot = new testcases.CWE486_Compare_Classes_by_Name.CWE486_Compare_Classes_by_Name__Helper();
        if (helperClassRoot.getClass().getSimpleName().equals(helperClass.getClass().getSimpleName()))
        {
            IO.writeLine("Classes are the same");
        }
        else
        {
            IO.writeLine("Classes are different");
        }
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
    {
        testcases.CWE486_Compare_Classes_by_Name.HelperClass.CWE486_Compare_Classes_by_Name__Helper helperClass = new testcases.CWE486_Compare_Classes_by_Name.HelperClass.CWE486_Compare_Classes_by_Name__Helper();
        testcases.CWE486_Compare_Classes_by_Name.CWE486_Compare_Classes_by_Name__Helper helperClassRoot = new testcases.CWE486_Compare_Classes_by_Name.CWE486_Compare_Classes_by_Name__Helper();
        if (helperClassRoot.getClass().equals(helperClass.getClass()))
        {
            IO.writeLine("Classes are the same");
        }
        else
        {
            IO.writeLine("Classes are different");
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
