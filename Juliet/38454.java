
package testcases.CWE129_Improper_Validation_of_Array_Index.s04;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE129_Improper_Validation_of_Array_Index__random_array_read_check_min_68a extends AbstractTestCase
{
    public static int data;
    public void bad() throws Throwable
    {
        data = (new SecureRandom()).nextInt();
        (new CWE129_Improper_Validation_of_Array_Index__random_array_read_check_min_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        data = 2;
        (new CWE129_Improper_Validation_of_Array_Index__random_array_read_check_min_68b()).goodG2BSink();
    }
    private void goodB2G() throws Throwable
    {
        data = (new SecureRandom()).nextInt();
        (new CWE129_Improper_Validation_of_Array_Index__random_array_read_check_min_68b()).goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
