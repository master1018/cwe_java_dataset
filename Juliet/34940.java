
package testcases.CWE78_OS_Command_Injection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE78_OS_Command_Injection__Environment_68a extends AbstractTestCase
{
    public static String data;
    public void bad() throws Throwable
    {
        data = System.getenv("ADD");
        (new CWE78_OS_Command_Injection__Environment_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        data = "foo";
        (new CWE78_OS_Command_Injection__Environment_68b()).goodG2BSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
