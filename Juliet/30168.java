
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__Integer_81_goodB2G extends CWE476_NULL_Pointer_Dereference__Integer_81_base
{
    public void action(Integer data ) throws Throwable
    {
        if (data != null)
        {
            IO.writeLine("" + data.toString());
        }
        else
        {
            IO.writeLine("data is null");
        }
    }
}
