
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__System_getProperty_equals_66a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = System.getProperty("CWE690");
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE690_NULL_Deref_From_Return__System_getProperty_equals_66b()).badSink(dataArray  );
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
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE690_NULL_Deref_From_Return__System_getProperty_equals_66b()).goodG2BSink(dataArray  );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = System.getProperty("CWE690");
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE690_NULL_Deref_From_Return__System_getProperty_equals_66b()).goodB2GSink(dataArray  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
