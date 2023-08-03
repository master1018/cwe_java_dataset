
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__System_getProperty_trim_68a extends AbstractTestCase
{
    public static String data;
    public void bad() throws Throwable
    {
        data = System.getProperty("CWE690");
        (new CWE690_NULL_Deref_From_Return__System_getProperty_trim_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        data = "CWE690";
        (new CWE690_NULL_Deref_From_Return__System_getProperty_trim_68b()).goodG2BSink();
    }
    private void goodB2G() throws Throwable
    {
        data = System.getProperty("CWE690");
        (new CWE690_NULL_Deref_From_Return__System_getProperty_trim_68b()).goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
