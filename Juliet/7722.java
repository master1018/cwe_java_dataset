
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__String_68b
{
    public void badSink() throws Throwable
    {
        String data = CWE476_NULL_Pointer_Dereference__String_68a.data;
        IO.writeLine("" + data.length());
    }
    public void goodG2BSink() throws Throwable
    {
        String data = CWE476_NULL_Pointer_Dereference__String_68a.data;
        IO.writeLine("" + data.length());
    }
    public void goodB2GSink() throws Throwable
    {
        String data = CWE476_NULL_Pointer_Dereference__String_68a.data;
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
