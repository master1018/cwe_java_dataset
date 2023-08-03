
package testcases.CWE129_Improper_Validation_of_Array_Index.s05;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__URLConnection_array_read_check_max_81_bad extends CWE129_Improper_Validation_of_Array_Index__URLConnection_array_read_check_max_81_base
{
    public void action(int data ) throws Throwable
    {
        int array[] = { 0, 1, 2, 3, 4 };
        if (data < array.length)
        {
            IO.writeLine(array[data]);
        }
        else
        {
            IO.writeLine("Array index out of bounds");
        }
    }
}
