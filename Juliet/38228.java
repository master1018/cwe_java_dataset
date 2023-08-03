
package testcases.CWE15_External_Control_of_System_or_Configuration_Setting;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE15_External_Control_of_System_or_Configuration_Setting__Property_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        CWE15_External_Control_of_System_or_Configuration_Setting__Property_81_base baseObject = new CWE15_External_Control_of_System_or_Configuration_Setting__Property_81_bad();
        baseObject.action(data );
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = "foo";
        CWE15_External_Control_of_System_or_Configuration_Setting__Property_81_base baseObject = new CWE15_External_Control_of_System_or_Configuration_Setting__Property_81_goodG2B();
        baseObject.action(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
