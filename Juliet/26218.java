
package testcases.CWE129_Improper_Validation_of_Array_Index.s05;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__random_array_size_66b
{
    public void badSink(int dataArray[] ) throws Throwable
    {
        int data = dataArray[2];
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
    public void goodG2BSink(int dataArray[] ) throws Throwable
    {
        int data = dataArray[2];
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
    public void goodB2GSink(int dataArray[] ) throws Throwable
    {
        int data = dataArray[2];
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