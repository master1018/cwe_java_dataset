
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__StringBuilder_71b
{
    public void badSink(Object dataObject ) throws Throwable
    {
        StringBuilder data = (StringBuilder)dataObject;
        IO.writeLine("" + data.length());
    }
    public void goodG2BSink(Object dataObject ) throws Throwable
    {
        StringBuilder data = (StringBuilder)dataObject;
        IO.writeLine("" + data.length());
    }
    public void goodB2GSink(Object dataObject ) throws Throwable
    {
        StringBuilder data = (StringBuilder)dataObject;
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
