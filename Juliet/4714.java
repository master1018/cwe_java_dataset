
package testcases.CWE129_Improper_Validation_of_Array_Index.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__console_readLine_array_read_check_min_81_bad extends CWE129_Improper_Validation_of_Array_Index__console_readLine_array_read_check_min_81_base
{
    public void action(int data ) throws Throwable
    {
        int array[] = { 0, 1, 2, 3, 4 };
        if (data >= 0)
        {
            IO.writeLine(array[data]);
        }
        else
        {
            IO.writeLine("Array index out of bounds");
        }
    }
}
