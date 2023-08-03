
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__Integer_67b
{
    public void badSink(CWE476_NULL_Pointer_Dereference__Integer_67a.Container dataContainer ) throws Throwable
    {
        Integer data = dataContainer.containerOne;
        IO.writeLine("" + data.toString());
    }
    public void goodG2BSink(CWE476_NULL_Pointer_Dereference__Integer_67a.Container dataContainer ) throws Throwable
    {
        Integer data = dataContainer.containerOne;
        IO.writeLine("" + data.toString());
    }
    public void goodB2GSink(CWE476_NULL_Pointer_Dereference__Integer_67a.Container dataContainer ) throws Throwable
    {
        Integer data = dataContainer.containerOne;
        if (data != null)
        {
            IO.writeLine("" + data.toString());
        }
        else
        {
            IO.writeLine("data is null");
        }
    }
}
