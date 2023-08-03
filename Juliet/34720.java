
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__int_array_67a extends AbstractTestCase
{
    static class Container
    {
        public int [] containerOne;
    }
    public void bad() throws Throwable
    {
        int [] data;
        data = null;
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE476_NULL_Pointer_Dereference__int_array_67b()).badSink(dataContainer  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int [] data;
        data = new int[5];
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE476_NULL_Pointer_Dereference__int_array_67b()).goodG2BSink(dataContainer  );
    }
    private void goodB2G() throws Throwable
    {
        int [] data;
        data = null;
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE476_NULL_Pointer_Dereference__int_array_67b()).goodB2GSink(dataContainer  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
