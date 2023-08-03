
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_String_66b
{
    public void badSink(String dataArray[] ) throws Throwable
    {
        String data = dataArray[2];
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodG2BSink(String dataArray[] ) throws Throwable
    {
        String data = dataArray[2];
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodB2GSink(String dataArray[] ) throws Throwable
    {
        String data = dataArray[2];
        if (data != null)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
}
