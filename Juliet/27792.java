
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__System_getProperty_trim_67a extends AbstractTestCase
{
    static class Container
    {
        public String containerOne;
    }
    public void bad() throws Throwable
    {
        String data;
        data = System.getProperty("CWE690");
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE690_NULL_Deref_From_Return__System_getProperty_trim_67b()).badSink(dataContainer  );
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
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE690_NULL_Deref_From_Return__System_getProperty_trim_67b()).goodG2BSink(dataContainer  );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = System.getProperty("CWE690");
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE690_NULL_Deref_From_Return__System_getProperty_trim_67b()).goodB2GSink(dataContainer  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
