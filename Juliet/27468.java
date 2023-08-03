
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_StringBuilder_53d
{
    public void badSink(StringBuilder data ) throws Throwable
    {
        String stringTrimmed = data.toString().trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodG2BSink(StringBuilder data ) throws Throwable
    {
        String stringTrimmed = data.toString().trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodB2GSink(StringBuilder data ) throws Throwable
    {
        if (data != null)
        {
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
        }
    }
}
