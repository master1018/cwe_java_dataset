
package testcases.CWE546_Suspicious_Comment;
import testcasesupport.*;
public class CWE546_Suspicious_Comment__TODO_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        while(true)
        {
            IO.writeLine("This a test of the emergency broadcast system");
            break;
        }
    }
    private void good1() throws Throwable
    {
        while(true)
        {
            IO.writeLine("This a test of the emergency broadcast system");
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
