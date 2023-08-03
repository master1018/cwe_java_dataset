
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__String_66a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = null;
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE476_NULL_Pointer_Dereference__String_66b()).badSink(dataArray  );
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
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE476_NULL_Pointer_Dereference__String_66b()).goodG2BSink(dataArray  );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = null;
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE476_NULL_Pointer_Dereference__String_66b()).goodB2GSink(dataArray  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
