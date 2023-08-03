
package testcases.CWE129_Improper_Validation_of_Array_Index.s05;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__random_array_write_no_check_67b
{
    public void badSink(CWE129_Improper_Validation_of_Array_Index__random_array_write_no_check_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        int array[] = { 0, 1, 2, 3, 4 };
        array[data] = 42;
    }
    public void goodG2BSink(CWE129_Improper_Validation_of_Array_Index__random_array_write_no_check_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        int array[] = { 0, 1, 2, 3, 4 };
        array[data] = 42;
    }
    public void goodB2GSink(CWE129_Improper_Validation_of_Array_Index__random_array_write_no_check_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        int array[] = { 0, 1, 2, 3, 4 };
        if (data >= 0 && data < array.length)
        {
            array[data] = 42;
        }
        else
        {
            IO.writeLine("Array index out of bounds");
        }
    }
}
