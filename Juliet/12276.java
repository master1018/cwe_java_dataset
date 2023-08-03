
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__int_array_53a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int [] data;
        data = null;
        (new CWE476_NULL_Pointer_Dereference__int_array_53b()).badSink(data );
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
        (new CWE476_NULL_Pointer_Dereference__int_array_53b()).goodG2BSink(data );
    }
    private void goodB2G() throws Throwable
    {
        int [] data;
        data = null;
        (new CWE476_NULL_Pointer_Dereference__int_array_53b()).goodB2GSink(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
