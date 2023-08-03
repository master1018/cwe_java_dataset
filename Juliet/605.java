
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__System_getProperty_equals_61a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data = (new CWE690_NULL_Deref_From_Return__System_getProperty_equals_61b()).badSource();
        if(data.equals("CWE690"))
        {
            IO.writeLine("data is CWE690");
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        String data = (new CWE690_NULL_Deref_From_Return__System_getProperty_equals_61b()).goodG2BSource();
        if(data.equals("CWE690"))
        {
            IO.writeLine("data is CWE690");
        }
    }
    private void goodB2G() throws Throwable
    {
        String data = (new CWE690_NULL_Deref_From_Return__System_getProperty_equals_61b()).goodB2GSource();
        if("CWE690".equals(data))
        {
            IO.writeLine("data is CWE690");
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
