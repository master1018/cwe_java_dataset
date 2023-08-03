
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_StringBuilder_67a extends AbstractTestCase
{
    static class Container
    {
        public StringBuilder containerOne;
    }
    public void bad() throws Throwable
    {
        StringBuilder data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE690_NULL_Deref_From_Return__Class_StringBuilder_67b()).badSink(dataContainer  );
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
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE690_NULL_Deref_From_Return__Class_StringBuilder_67b()).goodG2BSink(dataContainer  );
    }
    private void goodB2G() throws Throwable
    {
        StringBuilder data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE690_NULL_Deref_From_Return__Class_StringBuilder_67b()).goodB2GSink(dataContainer  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
