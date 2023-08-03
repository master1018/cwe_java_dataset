
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_StringBuilder_66b
{
    public void badSink(StringBuilder dataArray[] ) throws Throwable
    {
        StringBuilder data = dataArray[2];
        String stringTrimmed = data.toString().trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodG2BSink(StringBuilder dataArray[] ) throws Throwable
    {
        StringBuilder data = dataArray[2];
        String stringTrimmed = data.toString().trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodB2GSink(StringBuilder dataArray[] ) throws Throwable
    {
        StringBuilder data = dataArray[2];
        if (data != null)
        {
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
        }
    }
}
