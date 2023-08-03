
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__int_array_61a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int [] data = (new CWE476_NULL_Pointer_Dereference__int_array_61b()).badSource();
        IO.writeLine("" + data.length);
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int [] data = (new CWE476_NULL_Pointer_Dereference__int_array_61b()).goodG2BSource();
        IO.writeLine("" + data.length);
    }
    private void goodB2G() throws Throwable
    {
        int [] data = (new CWE476_NULL_Pointer_Dereference__int_array_61b()).goodB2GSource();
        if (data != null)
        {
            IO.writeLine("" + data.length);
        }
        else
        {
            IO.writeLine("data is null");
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
