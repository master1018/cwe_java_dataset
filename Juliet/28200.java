
package testcases.CWE129_Improper_Validation_of_Array_Index.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__Environment_array_size_67b
{
    public void badSink(CWE129_Improper_Validation_of_Array_Index__Environment_array_size_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        int array[] = null;
        if (data >= 0)
        {
            array = new int[data];
        }
        else
        {
            IO.writeLine("Array size is negative");
        }
        array[0] = 5;
        IO.writeLine(array[0]);
    }
    public void goodG2BSink(CWE129_Improper_Validation_of_Array_Index__Environment_array_size_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        int array[] = null;
        if (data >= 0)
        {
            array = new int[data];
        }
        else
        {
            IO.writeLine("Array size is negative");
        }
        array[0] = 5;
        IO.writeLine(array[0]);
    }
    public void goodB2GSink(CWE129_Improper_Validation_of_Array_Index__Environment_array_size_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        int array[] = null;
        if (data > 0)
        {
            array = new int[data];
        }
        else
        {
            IO.writeLine("Array size is negative");
        }
        array[0] = 5;
        IO.writeLine(array[0]);
    }
}
