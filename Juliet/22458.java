
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_StringBuilder_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        StringBuilder data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
        CWE690_NULL_Deref_From_Return__Class_StringBuilder_81_base baseObject = new CWE690_NULL_Deref_From_Return__Class_StringBuilder_81_bad();
        baseObject.action(data );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        StringBuilder data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderGood();
        CWE690_NULL_Deref_From_Return__Class_StringBuilder_81_base baseObject = new CWE690_NULL_Deref_From_Return__Class_StringBuilder_81_goodG2B();
        baseObject.action(data );
    }
    private void goodB2G() throws Throwable
    {
        StringBuilder data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
        CWE690_NULL_Deref_From_Return__Class_StringBuilder_81_base baseObject = new CWE690_NULL_Deref_From_Return__Class_StringBuilder_81_goodB2G();
        baseObject.action(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
