
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__String_51a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = null;
        (new CWE476_NULL_Pointer_Dereference__String_51b()).badSink(data  );
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
        (new CWE476_NULL_Pointer_Dereference__String_51b()).goodG2BSink(data  );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = null;
        (new CWE476_NULL_Pointer_Dereference__String_51b()).goodB2GSink(data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
