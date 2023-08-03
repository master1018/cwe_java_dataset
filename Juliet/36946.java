
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__StringBuilder_71a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        StringBuilder data;
        data = null;
        (new CWE476_NULL_Pointer_Dereference__StringBuilder_71b()).badSink((Object)data  );
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
        (new CWE476_NULL_Pointer_Dereference__StringBuilder_71b()).goodG2BSink((Object)data  );
    }
    private void goodB2G() throws Throwable
    {
        StringBuilder data;
        data = null;
        (new CWE476_NULL_Pointer_Dereference__StringBuilder_71b()).goodB2GSink((Object)data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
