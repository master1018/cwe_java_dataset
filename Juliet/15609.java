
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__StringBuilder_53d
{
    public void badSink(StringBuilder data ) throws Throwable
    {
        IO.writeLine("" + data.length());
    }
    public void goodG2BSink(StringBuilder data ) throws Throwable
    {
        IO.writeLine("" + data.length());
    }
    public void goodB2GSink(StringBuilder data ) throws Throwable
    {
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
