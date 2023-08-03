
package testcases.CWE129_Improper_Validation_of_Array_Index.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_write_no_check_61b
{
    public int badSource() throws Throwable
    {
        int data;
        data = -1;
        return data;
    }
    public int goodG2BSource() throws Throwable
    {
        int data;
        data = 2;
        return data;
    }
    public int goodB2GSource() throws Throwable
    {
        int data;
        data = -1;
        return data;
    }
}
