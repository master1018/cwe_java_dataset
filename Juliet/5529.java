
package testcases.CWE606_Unchecked_Loop_Condition;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE606_Unchecked_Loop_Condition__Property_68a extends AbstractTestCase
{
    public static String data;
    public void bad() throws Throwable
    {
        data = System.getProperty("user.home");
        (new CWE606_Unchecked_Loop_Condition__Property_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        data = "5";
        (new CWE606_Unchecked_Loop_Condition__Property_68b()).goodG2BSink();
    }
    private void goodB2G() throws Throwable
    {
        data = System.getProperty("user.home");
        (new CWE606_Unchecked_Loop_Condition__Property_68b()).goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
