
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__StringBuilder_68b
{
    public void badSink() throws Throwable
    {
        StringBuilder data = CWE476_NULL_Pointer_Dereference__StringBuilder_68a.data;
        IO.writeLine("" + data.length());
    }
    public void goodG2BSink() throws Throwable
    {
        StringBuilder data = CWE476_NULL_Pointer_Dereference__StringBuilder_68a.data;
        IO.writeLine("" + data.length());
    }
    public void goodB2GSink() throws Throwable
    {
        StringBuilder data = CWE476_NULL_Pointer_Dereference__StringBuilder_68a.data;
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
