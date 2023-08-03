
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import java.util.Vector;
public class CWE690_NULL_Deref_From_Return__Class_StringBuilder_72b
{
    public void badSink(Vector<StringBuilder> dataVector ) throws Throwable
    {
        StringBuilder data = dataVector.remove(2);
        String stringTrimmed = data.toString().trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodG2BSink(Vector<StringBuilder> dataVector ) throws Throwable
    {
        StringBuilder data = dataVector.remove(2);
        String stringTrimmed = data.toString().trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodB2GSink(Vector<StringBuilder> dataVector ) throws Throwable
    {
        StringBuilder data = dataVector.remove(2);
        if (data != null)
        {
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
        }
    }
}
