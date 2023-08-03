
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
import java.util.Vector;
public class CWE476_NULL_Pointer_Dereference__StringBuilder_72b
{
    public void badSink(Vector<StringBuilder> dataVector ) throws Throwable
    {
        StringBuilder data = dataVector.remove(2);
        IO.writeLine("" + data.length());
    }
    public void goodG2BSink(Vector<StringBuilder> dataVector ) throws Throwable
    {
        StringBuilder data = dataVector.remove(2);
        IO.writeLine("" + data.length());
    }
    public void goodB2GSink(Vector<StringBuilder> dataVector ) throws Throwable
    {
        StringBuilder data = dataVector.remove(2);
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
