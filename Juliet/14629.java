
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__Properties_getProperty_trim_81_bad extends CWE690_NULL_Deref_From_Return__Properties_getProperty_trim_81_base
{
    public void action(String data ) throws Throwable
    {
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
}
