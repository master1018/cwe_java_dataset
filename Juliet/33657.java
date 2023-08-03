
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__int_array_52c
{
    public void badSink(int [] data ) throws Throwable
    {
        IO.writeLine("" + data.length);
    }
    public void goodG2BSink(int [] data ) throws Throwable
    {
        IO.writeLine("" + data.length);
    }
    public void goodB2GSink(int [] data ) throws Throwable
    {
        if (data != null)
        {
            IO.writeLine("" + data.length);
        }
        else
        {
            IO.writeLine("data is null");
        }
    }
}
