
package testcases.CWE129_Improper_Validation_of_Array_Index.s04;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE129_Improper_Validation_of_Array_Index__random_array_read_check_min_53a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = (new SecureRandom()).nextInt();
        (new CWE129_Improper_Validation_of_Array_Index__random_array_read_check_min_53b()).badSink(data );
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
        (new CWE129_Improper_Validation_of_Array_Index__random_array_read_check_min_53b()).goodG2BSink(data );
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = (new SecureRandom()).nextInt();
        (new CWE129_Improper_Validation_of_Array_Index__random_array_read_check_min_53b()).goodB2GSink(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}