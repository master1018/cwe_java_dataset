
package testcases.CWE129_Improper_Validation_of_Array_Index.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__large_fixed_array_size_61a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data = (new CWE129_Improper_Validation_of_Array_Index__large_fixed_array_size_61b()).badSource();
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
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int data = (new CWE129_Improper_Validation_of_Array_Index__large_fixed_array_size_61b()).goodG2BSource();
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
    private void goodB2G() throws Throwable
    {
        int data = (new CWE129_Improper_Validation_of_Array_Index__large_fixed_array_size_61b()).goodB2GSource();
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
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
