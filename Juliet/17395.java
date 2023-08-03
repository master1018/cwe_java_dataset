
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_String_68b
{
    public void badSink() throws Throwable
    {
        String data = CWE690_NULL_Deref_From_Return__Class_String_68a.data;
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodG2BSink() throws Throwable
    {
        String data = CWE690_NULL_Deref_From_Return__Class_String_68a.data;
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodB2GSink() throws Throwable
    {
        String data = CWE690_NULL_Deref_From_Return__Class_String_68a.data;
        if (data != null)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
}
