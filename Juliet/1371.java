
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__Integer_68b
{
    public void badSink() throws Throwable
    {
        Integer data = CWE476_NULL_Pointer_Dereference__Integer_68a.data;
        IO.writeLine("" + data.toString());
    }
    public void goodG2BSink() throws Throwable
    {
        Integer data = CWE476_NULL_Pointer_Dereference__Integer_68a.data;
        IO.writeLine("" + data.toString());
    }
    public void goodB2GSink() throws Throwable
    {
        Integer data = CWE476_NULL_Pointer_Dereference__Integer_68a.data;
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
