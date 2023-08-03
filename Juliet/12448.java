
package testcases.CWE129_Improper_Validation_of_Array_Index.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_read_check_max_68a extends AbstractTestCase
{
    public static int data;
    public void bad() throws Throwable
    {
        data = -1;
        (new CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_read_check_max_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        data = 2;
        (new CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_read_check_max_68b()).goodG2BSink();
    }
    private void goodB2G() throws Throwable
    {
        data = -1;
        (new CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_read_check_max_68b()).goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
