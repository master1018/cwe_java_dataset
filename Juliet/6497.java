
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_long_81_bad extends CWE563_Unused_Variable__unused_value_long_81_base
{
    public void action(long data ) throws Throwable
    {
        data = 10L;
        IO.writeLine("" + data);
    }
}
