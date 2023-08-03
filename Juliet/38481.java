
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_int_81_bad extends CWE563_Unused_Variable__unused_value_int_81_base
{
    public void action(int data ) throws Throwable
    {
        data = 10;
        IO.writeLine("" + data);
    }
}
