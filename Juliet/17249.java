
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import java.util.Vector;
public class CWE690_NULL_Deref_From_Return__Class_StringBuilder_72a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        StringBuilder data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
        Vector<StringBuilder> dataVector = new Vector<StringBuilder>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE690_NULL_Deref_From_Return__Class_StringBuilder_72b()).badSink(dataVector  );
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
        Vector<StringBuilder> dataVector = new Vector<StringBuilder>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE690_NULL_Deref_From_Return__Class_StringBuilder_72b()).goodG2BSink(dataVector  );
    }
    private void goodB2G() throws Throwable
    {
        StringBuilder data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
        Vector<StringBuilder> dataVector = new Vector<StringBuilder>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE690_NULL_Deref_From_Return__Class_StringBuilder_72b()).goodB2GSink(dataVector  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
