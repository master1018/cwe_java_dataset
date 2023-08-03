
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_StringBuilder_68b
{
    public void badSink() throws Throwable
    {
        StringBuilder data = CWE690_NULL_Deref_From_Return__Class_StringBuilder_68a.data;
        String stringTrimmed = data.toString().trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodG2BSink() throws Throwable
    {
        StringBuilder data = CWE690_NULL_Deref_From_Return__Class_StringBuilder_68a.data;
        String stringTrimmed = data.toString().trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodB2GSink() throws Throwable
    {
        StringBuilder data = CWE690_NULL_Deref_From_Return__Class_StringBuilder_68a.data;
        if (data != null)
        {
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
        }
    }
}
