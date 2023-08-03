
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__StringBuilder_66a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        StringBuilder data;
        data = null;
        StringBuilder[] dataArray = new StringBuilder[5];
        dataArray[2] = data;
        (new CWE476_NULL_Pointer_Dereference__StringBuilder_66b()).badSink(dataArray  );
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
        StringBuilder[] dataArray = new StringBuilder[5];
        dataArray[2] = data;
        (new CWE476_NULL_Pointer_Dereference__StringBuilder_66b()).goodG2BSink(dataArray  );
    }
    private void goodB2G() throws Throwable
    {
        StringBuilder data;
        data = null;
        StringBuilder[] dataArray = new StringBuilder[5];
        dataArray[2] = data;
        (new CWE476_NULL_Pointer_Dereference__StringBuilder_66b()).goodB2GSink(dataArray  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
