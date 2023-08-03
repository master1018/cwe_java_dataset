
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__System_getProperty_equals_41 extends AbstractTestCase
{
    private void badSink(String data ) throws Throwable
    {
        if(data.equals("CWE690"))
        {
            IO.writeLine("data is CWE690");
        }
    }
    public void bad() throws Throwable
    {
        String data;
        data = System.getProperty("CWE690");
        badSink(data  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2BSink(String data ) throws Throwable
    {
        if(data.equals("CWE690"))
        {
            IO.writeLine("data is CWE690");
        }
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = "CWE690";
        goodG2BSink(data  );
    }
    private void goodB2GSink(String data ) throws Throwable
    {
        if("CWE690".equals(data))
        {
            IO.writeLine("data is CWE690");
        }
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = System.getProperty("CWE690");
        goodB2GSink(data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
