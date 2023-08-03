
package testcases.CWE129_Improper_Validation_of_Array_Index.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__PropertiesFile_array_read_check_min_22b
{
    public void badSink(int data ) throws Throwable
    {
        if (CWE129_Improper_Validation_of_Array_Index__PropertiesFile_array_read_check_min_22a.badPublicStatic)
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
        else
        {
            data = 0;
        }
    }
    public void goodB2G1Sink(int data ) throws Throwable
    {
        if (CWE129_Improper_Validation_of_Array_Index__PropertiesFile_array_read_check_min_22a.goodB2G1PublicStatic)
        {
            data = 0;
        }
        else
        {
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
    public void goodB2G2Sink(int data ) throws Throwable
    {
        if (CWE129_Improper_Validation_of_Array_Index__PropertiesFile_array_read_check_min_22a.goodB2G2PublicStatic)
        {
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
        else
        {
            data = 0;
        }
    }
    public void goodG2BSink(int data ) throws Throwable
    {
        if (CWE129_Improper_Validation_of_Array_Index__PropertiesFile_array_read_check_min_22a.goodG2BPublicStatic)
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
        else
        {
            data = 0;
        }
    }
}
