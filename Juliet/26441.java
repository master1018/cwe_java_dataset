
package testcases.CWE129_Improper_Validation_of_Array_Index.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__large_fixed_array_read_no_check_81_goodG2B extends CWE129_Improper_Validation_of_Array_Index__large_fixed_array_read_no_check_81_base
{
    public void action(int data ) throws Throwable
    {
        int array[] = { 0, 1, 2, 3, 4 };
        IO.writeLine(array[data]);
    }
}
