
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_StringBuilder_81_goodB2G extends CWE690_NULL_Deref_From_Return__Class_StringBuilder_81_base
{
    public void action(StringBuilder data ) throws Throwable
    {
        if (data != null)
        {
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
        }
    }
}
