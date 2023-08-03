
package testcases.CWE643_Xpath_Injection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE643_Xpath_Injection__Environment_68a extends AbstractTestCase
{
    public static String data;
    public void bad() throws Throwable
    {
        data = System.getenv("ADD");
        (new CWE643_Xpath_Injection__Environment_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        data = "foo";
        (new CWE643_Xpath_Injection__Environment_68b()).goodG2BSink();
    }
    private void goodB2G() throws Throwable
    {
        data = System.getenv("ADD");
        (new CWE643_Xpath_Injection__Environment_68b()).goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
