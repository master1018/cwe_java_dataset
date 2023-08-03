
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__String_71a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = null;
        (new CWE476_NULL_Pointer_Dereference__String_71b()).badSink((Object)data  );
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
        (new CWE476_NULL_Pointer_Dereference__String_71b()).goodG2BSink((Object)data  );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = null;
        (new CWE476_NULL_Pointer_Dereference__String_71b()).goodB2GSink((Object)data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
