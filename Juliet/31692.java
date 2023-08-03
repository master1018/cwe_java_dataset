
package testcases.CWE129_Improper_Validation_of_Array_Index.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__connect_tcp_array_write_no_check_61a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data = (new CWE129_Improper_Validation_of_Array_Index__connect_tcp_array_write_no_check_61b()).badSource();
        int array[] = { 0, 1, 2, 3, 4 };
        array[data] = 42;
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int data = (new CWE129_Improper_Validation_of_Array_Index__connect_tcp_array_write_no_check_61b()).goodG2BSource();
        int array[] = { 0, 1, 2, 3, 4 };
        array[data] = 42;
    }
    private void goodB2G() throws Throwable
    {
        int data = (new CWE129_Improper_Validation_of_Array_Index__connect_tcp_array_write_no_check_61b()).goodB2GSource();
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
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
