
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__int_array_71b
{
    public void badSink(Object dataObject ) throws Throwable
    {
        int [] data = (int [])dataObject;
        IO.writeLine("" + data.length);
    }
    public void goodG2BSink(Object dataObject ) throws Throwable
    {
        int [] data = (int [])dataObject;
        IO.writeLine("" + data.length);
    }
    public void goodB2GSink(Object dataObject ) throws Throwable
    {
        int [] data = (int [])dataObject;
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
