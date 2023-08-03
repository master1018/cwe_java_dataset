
package testcases.CWE369_Divide_by_Zero.s01;
import testcasesupport.*;
public class CWE369_Divide_by_Zero__float_Property_divide_81_goodG2B extends CWE369_Divide_by_Zero__float_Property_divide_81_base
{
    public void action(float data ) throws Throwable
    {
        int result = (int)(100.0 / data);
        IO.writeLine(result);
    }
}
