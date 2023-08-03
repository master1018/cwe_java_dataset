
package testcases.CWE129_Improper_Validation_of_Array_Index.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_write_no_check_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = -1;
        CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_write_no_check_81_base baseObject = new CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_write_no_check_81_bad();
        baseObject.action(data );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_write_no_check_81_base baseObject = new CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_write_no_check_81_goodG2B();
        baseObject.action(data );
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = -1;
        CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_write_no_check_81_base baseObject = new CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_write_no_check_81_goodB2G();
        baseObject.action(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
