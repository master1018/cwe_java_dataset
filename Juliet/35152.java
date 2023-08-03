
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
import java.util.Vector;
public class CWE476_NULL_Pointer_Dereference__int_array_72b
{
    public void badSink(Vector<int []> dataVector ) throws Throwable
    {
        int [] data = dataVector.remove(2);
        IO.writeLine("" + data.length);
    }
    public void goodG2BSink(Vector<int []> dataVector ) throws Throwable
    {
        int [] data = dataVector.remove(2);
        IO.writeLine("" + data.length);
    }
    public void goodB2GSink(Vector<int []> dataVector ) throws Throwable
    {
        int [] data = dataVector.remove(2);
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
