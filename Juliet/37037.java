
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__Integer_66b
{
    public void badSink(Integer dataArray[] ) throws Throwable
    {
        Integer data = dataArray[2];
        IO.writeLine("" + data.toString());
    }
    public void goodG2BSink(Integer dataArray[] ) throws Throwable
    {
        Integer data = dataArray[2];
        IO.writeLine("" + data.toString());
    }
    public void goodB2GSink(Integer dataArray[] ) throws Throwable
    {
        Integer data = dataArray[2];
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
