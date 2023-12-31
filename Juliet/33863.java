
package testcases.CWE129_Improper_Validation_of_Array_Index.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__large_fixed_array_write_no_check_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        while (true)
        {
            data = 100;
            break;
        }
        while (true)
        {
            int array[] = { 0, 1, 2, 3, 4 };
            array[data] = 42;
            break;
        }
    }
    private void goodG2B() throws Throwable
    {
        int data;
        while (true)
        {
            data = 2;
            break;
        }
        while (true)
        {
            int array[] = { 0, 1, 2, 3, 4 };
            array[data] = 42;
            break;
        }
    }
    private void goodB2G() throws Throwable
    {
        int data;
        while (true)
        {
            data = 100;
            break;
        }
        while (true)
        {
            int array[] = { 0, 1, 2, 3, 4 };
            if (data >= 0 && data < array.length)
            {
                array[data] = 42;
            }
            else
            {
                IO.writeLine("Array index out of bounds");
            }
            break;
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
