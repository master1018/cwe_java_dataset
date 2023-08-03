
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__int_array_81_bad extends CWE476_NULL_Pointer_Dereference__int_array_81_base
{
    public void action(int [] data ) throws Throwable
    {
        IO.writeLine("" + data.length);
    }
}
