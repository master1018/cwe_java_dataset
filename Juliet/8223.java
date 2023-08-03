
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__System_getProperty_equals_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = System.getProperty("CWE690");
        CWE690_NULL_Deref_From_Return__System_getProperty_equals_81_base baseObject = new CWE690_NULL_Deref_From_Return__System_getProperty_equals_81_bad();
        baseObject.action(data );
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
        CWE690_NULL_Deref_From_Return__System_getProperty_equals_81_base baseObject = new CWE690_NULL_Deref_From_Return__System_getProperty_equals_81_goodG2B();
        baseObject.action(data );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = System.getProperty("CWE690");
        CWE690_NULL_Deref_From_Return__System_getProperty_equals_81_base baseObject = new CWE690_NULL_Deref_From_Return__System_getProperty_equals_81_goodB2G();
        baseObject.action(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
