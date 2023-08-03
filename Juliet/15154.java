
package testcases.CWE643_Xpath_Injection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE643_Xpath_Injection__Property_22a extends AbstractTestCase
{
    public static boolean badPublicStatic = false;
    public void bad() throws Throwable
    {
        String data = null;
        data = System.getProperty("user.home");
        badPublicStatic = true;
        (new CWE643_Xpath_Injection__Property_22b()).badSink(data );
    }
    public static boolean goodB2G1PublicStatic = false;
    public static boolean goodB2G2PublicStatic = false;
    public static boolean goodG2BPublicStatic = false;
    public void good() throws Throwable
    {
        goodB2G1();
        goodB2G2();
        goodG2B();
    }
    private void goodB2G1() throws Throwable
    {
        String data = null;
        data = System.getProperty("user.home");
        goodB2G1PublicStatic = false;
        (new CWE643_Xpath_Injection__Property_22b()).goodB2G1Sink(data );
    }
    private void goodB2G2() throws Throwable
    {
        String data = null;
        data = System.getProperty("user.home");
        goodB2G2PublicStatic = true;
        (new CWE643_Xpath_Injection__Property_22b()).goodB2G2Sink(data );
    }
    private void goodG2B() throws Throwable
    {
        String data = null;
        data = "foo";
        goodG2BPublicStatic = true;
        (new CWE643_Xpath_Injection__Property_22b()).goodG2BSink(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
