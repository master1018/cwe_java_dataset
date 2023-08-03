
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__Integer_54a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        Integer data;
        data = null;
        (new CWE476_NULL_Pointer_Dereference__Integer_54b()).badSink(data );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        Integer data;
        data = Integer.valueOf(5);
        (new CWE476_NULL_Pointer_Dereference__Integer_54b()).goodG2BSink(data );
    }
    private void goodB2G() throws Throwable
    {
        Integer data;
        data = null;
        (new CWE476_NULL_Pointer_Dereference__Integer_54b()).goodB2GSink(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
