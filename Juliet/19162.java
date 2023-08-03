
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__Integer_53d
{
    public void badSink(Integer data ) throws Throwable
    {
        IO.writeLine("" + data.toString());
    }
    public void goodG2BSink(Integer data ) throws Throwable
    {
        IO.writeLine("" + data.toString());
    }
    public void goodB2GSink(Integer data ) throws Throwable
    {
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
