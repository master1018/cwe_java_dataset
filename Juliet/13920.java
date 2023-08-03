
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__Properties_getProperty_trim_67b
{
    public void badSink(CWE690_NULL_Deref_From_Return__Properties_getProperty_trim_67a.Container dataContainer ) throws Throwable
    {
        String data = dataContainer.containerOne;
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodG2BSink(CWE690_NULL_Deref_From_Return__Properties_getProperty_trim_67a.Container dataContainer ) throws Throwable
    {
        String data = dataContainer.containerOne;
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodB2GSink(CWE690_NULL_Deref_From_Return__Properties_getProperty_trim_67a.Container dataContainer ) throws Throwable
    {
        String data = dataContainer.containerOne;
        if (data != null)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
}
