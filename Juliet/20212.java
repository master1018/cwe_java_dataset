
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_String_66a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBad();
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE690_NULL_Deref_From_Return__Class_String_66b()).badSink(dataArray  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringGood();
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE690_NULL_Deref_From_Return__Class_String_66b()).goodG2BSink(dataArray  );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBad();
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE690_NULL_Deref_From_Return__Class_String_66b()).goodB2GSink(dataArray  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
