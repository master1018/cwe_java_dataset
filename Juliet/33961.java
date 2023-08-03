
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_StringBuilder_71b
{
    public void badSink(Object dataObject ) throws Throwable
    {
        StringBuilder data = (StringBuilder)dataObject;
        String stringTrimmed = data.toString().trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodG2BSink(Object dataObject ) throws Throwable
    {
        StringBuilder data = (StringBuilder)dataObject;
        String stringTrimmed = data.toString().trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodB2GSink(Object dataObject ) throws Throwable
    {
        StringBuilder data = (StringBuilder)dataObject;
        if (data != null)
        {
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
        }
    }
}
