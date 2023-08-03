
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__StringBuilder_54a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        StringBuilder data;
        data = null;
        (new CWE476_NULL_Pointer_Dereference__StringBuilder_54b()).badSink(data );
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
        (new CWE476_NULL_Pointer_Dereference__StringBuilder_54b()).goodG2BSink(data );
    }
    private void goodB2G() throws Throwable
    {
        StringBuilder data;
        data = null;
        (new CWE476_NULL_Pointer_Dereference__StringBuilder_54b()).goodB2GSink(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
