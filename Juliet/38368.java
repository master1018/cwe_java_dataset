
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__StringBuilder_66b
{
    public void badSink(StringBuilder dataArray[] ) throws Throwable
    {
        StringBuilder data = dataArray[2];
        IO.writeLine("" + data.length());
    }
    public void goodG2BSink(StringBuilder dataArray[] ) throws Throwable
    {
        StringBuilder data = dataArray[2];
        IO.writeLine("" + data.length());
    }
    public void goodB2GSink(StringBuilder dataArray[] ) throws Throwable
    {
        StringBuilder data = dataArray[2];
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
