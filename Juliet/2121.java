
package testcases.CWE546_Suspicious_Comment;
import testcasesupport.*;
public class CWE546_Suspicious_Comment__TODO_03 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (5 == 5)
        {
            IO.writeLine("This a test of the emergency broadcast system");
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
            IO.writeLine("This a test of the emergency broadcast system");
        }
    }
    private void good2() throws Throwable
    {
        if (5 == 5)
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
