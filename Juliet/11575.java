
package testcases.CWE643_Xpath_Injection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE643_Xpath_Injection__Property_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        CWE643_Xpath_Injection__Property_81_base baseObject = new CWE643_Xpath_Injection__Property_81_bad();
        baseObject.action(data );
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
        CWE643_Xpath_Injection__Property_81_base baseObject = new CWE643_Xpath_Injection__Property_81_goodG2B();
        baseObject.action(data );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        CWE643_Xpath_Injection__Property_81_base baseObject = new CWE643_Xpath_Injection__Property_81_goodB2G();
        baseObject.action(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
