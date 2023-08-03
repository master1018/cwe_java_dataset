
package testcases.CWE129_Improper_Validation_of_Array_Index.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__File_array_write_no_check_81_goodG2B extends CWE129_Improper_Validation_of_Array_Index__File_array_write_no_check_81_base
{
    public void action(int data ) throws Throwable
    {
        int array[] = { 0, 1, 2, 3, 4 };
        array[data] = 42;
    }
}
