
package testcases.CWE129_Improper_Validation_of_Array_Index.s04;
import testcasesupport.*;
import java.util.Vector;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__PropertiesFile_array_read_check_max_72b
{
    public void badSink(Vector<Integer> dataVector ) throws Throwable
    {
        int data = dataVector.remove(2);
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
    public void goodG2BSink(Vector<Integer> dataVector ) throws Throwable
    {
        int data = dataVector.remove(2);
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
    public void goodB2GSink(Vector<Integer> dataVector ) throws Throwable
    {
        int data = dataVector.remove(2);
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
