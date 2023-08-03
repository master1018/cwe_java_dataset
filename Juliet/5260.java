
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__Integer_81_bad extends CWE476_NULL_Pointer_Dereference__Integer_81_base
{
    public void action(Integer data ) throws Throwable
    {
        IO.writeLine("" + data.toString());
    }
}
