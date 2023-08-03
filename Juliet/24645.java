
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_StringBuilder_22a extends AbstractTestCase
{
    public static boolean badPublicStatic = false;
    public void bad() throws Throwable
    {
        StringBuilder data = null;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
        badPublicStatic = true;
        (new CWE690_NULL_Deref_From_Return__Class_StringBuilder_22b()).badSink(data );
    }
    public static boolean goodB2G1PublicStatic = false;
    public static boolean goodB2G2PublicStatic = false;
    public static boolean goodG2BPublicStatic = false;
    public void good() throws Throwable
    {
        goodB2G1();
        goodB2G2();
        goodG2B();
    }
    private void goodB2G1() throws Throwable
    {
        StringBuilder data = null;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
        goodB2G1PublicStatic = false;
        (new CWE690_NULL_Deref_From_Return__Class_StringBuilder_22b()).goodB2G1Sink(data );
    }
    private void goodB2G2() throws Throwable
    {
        StringBuilder data = null;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
        goodB2G2PublicStatic = true;
        (new CWE690_NULL_Deref_From_Return__Class_StringBuilder_22b()).goodB2G2Sink(data );
    }
    private void goodG2B() throws Throwable
    {
        StringBuilder data = null;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderGood();
        goodG2BPublicStatic = true;
        (new CWE690_NULL_Deref_From_Return__Class_StringBuilder_22b()).goodG2BSink(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
