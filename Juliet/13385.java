
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__StringBuilder_67a extends AbstractTestCase
{
    static class Container
    {
        public StringBuilder containerOne;
    }
    public void bad() throws Throwable
    {
        StringBuilder data;
        data = null;
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE476_NULL_Pointer_Dereference__StringBuilder_67b()).badSink(dataContainer  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        StringBuilder data;
        data = new StringBuilder();
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE476_NULL_Pointer_Dereference__StringBuilder_67b()).goodG2BSink(dataContainer  );
    }
    private void goodB2G() throws Throwable
    {
        StringBuilder data;
        data = null;
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE476_NULL_Pointer_Dereference__StringBuilder_67b()).goodB2GSink(dataContainer  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
