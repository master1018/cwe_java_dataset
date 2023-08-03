
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__String_66b
{
    public void badSink(String dataArray[] ) throws Throwable
    {
        String data = dataArray[2];
        IO.writeLine("" + data.length());
    }
    public void goodG2BSink(String dataArray[] ) throws Throwable
    {
        String data = dataArray[2];
        IO.writeLine("" + data.length());
    }
    public void goodB2GSink(String dataArray[] ) throws Throwable
    {
        String data = dataArray[2];
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
