
package testcases.CWE643_Xpath_Injection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE643_Xpath_Injection__Environment_53a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        (new CWE643_Xpath_Injection__Environment_53b()).badSink(data );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = "foo";
        (new CWE643_Xpath_Injection__Environment_53b()).goodG2BSink(data );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        (new CWE643_Xpath_Injection__Environment_53b()).goodB2GSink(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
