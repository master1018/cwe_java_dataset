
package testcases.CWE78_OS_Command_Injection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE78_OS_Command_Injection__Property_66a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE78_OS_Command_Injection__Property_66b()).badSink(dataArray  );
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = "foo";
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE78_OS_Command_Injection__Property_66b()).goodG2BSink(dataArray  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
