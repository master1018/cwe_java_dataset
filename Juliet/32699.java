
package testcases.CWE129_Improper_Validation_of_Array_Index.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__File_array_write_no_check_71b
{
    public void badSink(Object dataObject ) throws Throwable
    {
        int data = (Integer)dataObject;
        int array[] = { 0, 1, 2, 3, 4 };
        array[data] = 42;
    }
    public void goodG2BSink(Object dataObject ) throws Throwable
    {
        int data = (Integer)dataObject;
        int array[] = { 0, 1, 2, 3, 4 };
        array[data] = 42;
    }
    public void goodB2GSink(Object dataObject ) throws Throwable
    {
        int data = (Integer)dataObject;
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
