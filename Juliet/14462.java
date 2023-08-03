
package testcases.CWE89_SQL_Injection.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE89_SQL_Injection__Environment_prepareStatement_68a extends AbstractTestCase
{
    public static String data;
    public void bad() throws Throwable
    {
        data = System.getenv("ADD");
        (new CWE89_SQL_Injection__Environment_prepareStatement_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        data = "foo";
        (new CWE89_SQL_Injection__Environment_prepareStatement_68b()).goodG2BSink();
    }
    private void goodB2G() throws Throwable
    {
        data = System.getenv("ADD");
        (new CWE89_SQL_Injection__Environment_prepareStatement_68b()).goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
