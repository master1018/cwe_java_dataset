
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_StringBuilder_67b
{
    public void badSink(CWE690_NULL_Deref_From_Return__Class_StringBuilder_67a.Container dataContainer ) throws Throwable
    {
        StringBuilder data = dataContainer.containerOne;
        String stringTrimmed = data.toString().trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodG2BSink(CWE690_NULL_Deref_From_Return__Class_StringBuilder_67a.Container dataContainer ) throws Throwable
    {
        StringBuilder data = dataContainer.containerOne;
        String stringTrimmed = data.toString().trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodB2GSink(CWE690_NULL_Deref_From_Return__Class_StringBuilder_67a.Container dataContainer ) throws Throwable
    {
        StringBuilder data = dataContainer.containerOne;
        if (data != null)
        {
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
        }
    }
}
