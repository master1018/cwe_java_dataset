
package testcases.CWE129_Improper_Validation_of_Array_Index.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_write_no_check_66a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = -1;
        int[] dataArray = new int[5];
        dataArray[2] = data;
        (new CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_write_no_check_66b()).badSink(dataArray  );
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
        int[] dataArray = new int[5];
        dataArray[2] = data;
        (new CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_write_no_check_66b()).goodG2BSink(dataArray  );
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = -1;
        int[] dataArray = new int[5];
        dataArray[2] = data;
        (new CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_write_no_check_66b()).goodB2GSink(dataArray  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
