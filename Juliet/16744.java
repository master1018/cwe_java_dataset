
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__Integer_61a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        Integer data = (new CWE476_NULL_Pointer_Dereference__Integer_61b()).badSource();
        IO.writeLine("" + data.toString());
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        Integer data = (new CWE476_NULL_Pointer_Dereference__Integer_61b()).goodG2BSource();
        IO.writeLine("" + data.toString());
    }
    private void goodB2G() throws Throwable
    {
        Integer data = (new CWE476_NULL_Pointer_Dereference__Integer_61b()).goodB2GSource();
        if (data != null)
        {
            IO.writeLine("" + data.toString());
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
