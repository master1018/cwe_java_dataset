
package testcases.CWE129_Improper_Validation_of_Array_Index.s01;
import testcasesupport.*;
import java.util.Vector;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__connect_tcp_array_write_no_check_72b
{
    public void badSink(Vector<Integer> dataVector ) throws Throwable
    {
        int data = dataVector.remove(2);
        int array[] = { 0, 1, 2, 3, 4 };
        array[data] = 42;
    }
    public void goodG2BSink(Vector<Integer> dataVector ) throws Throwable
    {
        int data = dataVector.remove(2);
        int array[] = { 0, 1, 2, 3, 4 };
        array[data] = 42;
    }
    public void goodB2GSink(Vector<Integer> dataVector ) throws Throwable
    {
        int data = dataVector.remove(2);
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
