
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__Integer_71b
{
    public void badSink(Object dataObject ) throws Throwable
    {
        Integer data = (Integer)dataObject;
        IO.writeLine("" + data.toString());
    }
    public void goodG2BSink(Object dataObject ) throws Throwable
    {
        Integer data = (Integer)dataObject;
        IO.writeLine("" + data.toString());
    }
    public void goodB2GSink(Object dataObject ) throws Throwable
    {
        Integer data = (Integer)dataObject;
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
