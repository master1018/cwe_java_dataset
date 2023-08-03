
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_StringBuilder_81_goodG2B extends CWE563_Unused_Variable__unused_value_StringBuilder_81_base
{
    public void action(StringBuilder data ) throws Throwable
    {
        data = new StringBuilder("Reinitialize");
        IO.writeLine(data.toString());
    }
}
