
package testcases.CWE197_Numeric_Truncation_Error.s01;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__int_File_to_short_81_bad extends CWE197_Numeric_Truncation_Error__int_File_to_short_81_base
{
    public void action(int data ) throws Throwable
    {
        {
            IO.writeLine((short)data);
        }
    }
}
