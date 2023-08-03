
package testcases.CWE129_Improper_Validation_of_Array_Index.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__listen_tcp_array_read_check_min_67b
{
    public void badSink(CWE129_Improper_Validation_of_Array_Index__listen_tcp_array_read_check_min_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
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
    public void goodG2BSink(CWE129_Improper_Validation_of_Array_Index__listen_tcp_array_read_check_min_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
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
    public void goodB2GSink(CWE129_Improper_Validation_of_Array_Index__listen_tcp_array_read_check_min_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        int array[] = { 0, 1, 2, 3, 4 };
        if (data >= 0 && data < array.length)
        {
            IO.writeLine(array[data]);
        }
        else
        {
            IO.writeLine("Array index out of bounds");
        }
    }
}
