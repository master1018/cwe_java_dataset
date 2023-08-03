
package testcases.CWE546_Suspicious_Comment;
import testcasesupport.*;
public class CWE546_Suspicious_Comment__HACK_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        IO.writeLine("This a test of the emergency broadcast system");
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
    {
        IO.writeLine("This a test of the emergency broadcast system");
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
