
package testcases.CWE15_External_Control_of_System_or_Configuration_Setting;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE15_External_Control_of_System_or_Configuration_Setting__Property_68a extends AbstractTestCase
{
    public static String data;
    public void bad() throws Throwable
    {
        data = System.getProperty("user.home");
        (new CWE15_External_Control_of_System_or_Configuration_Setting__Property_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        data = "foo";
        (new CWE15_External_Control_of_System_or_Configuration_Setting__Property_68b()).goodG2BSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
