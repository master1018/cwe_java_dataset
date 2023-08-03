
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__System_getProperty_equals_31 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String dataCopy;
        {
            String data;
            data = System.getProperty("CWE690");
            dataCopy = data;
        }
        {
            String data = dataCopy;
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        String dataCopy;
        {
            String data;
            data = "CWE690";
            dataCopy = data;
        }
        {
            String data = dataCopy;
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
        }
    }
    private void goodB2G() throws Throwable
    {
        String dataCopy;
        {
            String data;
            data = System.getProperty("CWE690");
            dataCopy = data;
        }
        {
            String data = dataCopy;
            if("CWE690".equals(data))
            {
                IO.writeLine("data is CWE690");
            }
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
