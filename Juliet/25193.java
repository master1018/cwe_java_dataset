
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_String_68a extends AbstractTestCase
{
    public static String data;
    public void bad() throws Throwable
    {
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBad();
        (new CWE690_NULL_Deref_From_Return__Class_String_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringGood();
        (new CWE690_NULL_Deref_From_Return__Class_String_68b()).goodG2BSink();
    }
    private void goodB2G() throws Throwable
    {
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBad();
        (new CWE690_NULL_Deref_From_Return__Class_String_68b()).goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
