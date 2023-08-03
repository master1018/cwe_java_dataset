
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__Properties_getProperty_trim_52c
{
    public void badSink(String data ) throws Throwable
    {
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodG2BSink(String data ) throws Throwable
    {
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodB2GSink(String data ) throws Throwable
    {
        if (data != null)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
}
