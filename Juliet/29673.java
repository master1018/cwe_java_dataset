
package testcases.CWE546_Suspicious_Comment;
import testcasesupport.*;
public class CWE546_Suspicious_Comment__LATER_02 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (true)
        {
            IO.writeLine("This a test of the emergency broadcast system");
        }
    }
    private void good1() throws Throwable
    {
        if (false)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            IO.writeLine("This a test of the emergency broadcast system");
        }
    }
    private void good2() throws Throwable
    {
        if (true)
        {
            IO.writeLine("This a test of the emergency broadcast system");
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
