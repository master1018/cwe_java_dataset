
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__String_67a extends AbstractTestCase
{
    static class Container
    {
        public String containerOne;
    }
    public void bad() throws Throwable
    {
        String data;
        data = null;
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE476_NULL_Pointer_Dereference__String_67b()).badSink(dataContainer  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = "This is not null";
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE476_NULL_Pointer_Dereference__String_67b()).goodG2BSink(dataContainer  );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = null;
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE476_NULL_Pointer_Dereference__String_67b()).goodB2GSink(dataContainer  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
