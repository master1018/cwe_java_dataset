
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_StringBuilder_66a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        StringBuilder data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
        StringBuilder[] dataArray = new StringBuilder[5];
        dataArray[2] = data;
        (new CWE690_NULL_Deref_From_Return__Class_StringBuilder_66b()).badSink(dataArray  );
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
        StringBuilder[] dataArray = new StringBuilder[5];
        dataArray[2] = data;
        (new CWE690_NULL_Deref_From_Return__Class_StringBuilder_66b()).goodG2BSink(dataArray  );
    }
    private void goodB2G() throws Throwable
    {
        StringBuilder data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
        StringBuilder[] dataArray = new StringBuilder[5];
        dataArray[2] = data;
        (new CWE690_NULL_Deref_From_Return__Class_StringBuilder_66b()).goodB2GSink(dataArray  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
