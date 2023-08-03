
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__String_52c
{
    public void badSink(String data ) throws Throwable
    {
        IO.writeLine("" + data.length());
    }
    public void goodG2BSink(String data ) throws Throwable
    {
        IO.writeLine("" + data.length());
    }
    public void goodB2GSink(String data ) throws Throwable
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
