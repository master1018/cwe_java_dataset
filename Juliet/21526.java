
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__String_67b
{
    public void badSink(CWE476_NULL_Pointer_Dereference__String_67a.Container dataContainer ) throws Throwable
    {
        String data = dataContainer.containerOne;
        IO.writeLine("" + data.length());
    }
    public void goodG2BSink(CWE476_NULL_Pointer_Dereference__String_67a.Container dataContainer ) throws Throwable
    {
        String data = dataContainer.containerOne;
        IO.writeLine("" + data.length());
    }
    public void goodB2GSink(CWE476_NULL_Pointer_Dereference__String_67a.Container dataContainer ) throws Throwable
    {
        String data = dataContainer.containerOne;
        if (data != null)
        {
            IO.writeLine("" + data.length());
        }
        else
        {
            IO.writeLine("data is null");
        }
    }
}
