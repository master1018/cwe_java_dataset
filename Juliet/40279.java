
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_String_81_bad extends CWE563_Unused_Variable__unused_value_String_81_base
{
    public void action(String data ) throws Throwable
    {
        data = "Reinitialize";
        IO.writeLine(data);
    }
}
