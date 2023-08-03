
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__System_getProperty_trim_71a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = System.getProperty("CWE690");
        (new CWE690_NULL_Deref_From_Return__System_getProperty_trim_71b()).badSink((Object)data  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = "CWE690";
        (new CWE690_NULL_Deref_From_Return__System_getProperty_trim_71b()).goodG2BSink((Object)data  );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = System.getProperty("CWE690");
        (new CWE690_NULL_Deref_From_Return__System_getProperty_trim_71b()).goodB2GSink((Object)data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
