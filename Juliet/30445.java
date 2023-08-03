
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__String_68a extends AbstractTestCase
{
    public static String data;
    public void bad() throws Throwable
    {
        data = null;
        (new CWE476_NULL_Pointer_Dereference__String_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        data = "This is not null";
        (new CWE476_NULL_Pointer_Dereference__String_68b()).goodG2BSink();
    }
    private void goodB2G() throws Throwable
    {
        data = null;
        (new CWE476_NULL_Pointer_Dereference__String_68b()).goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
