
package testcases.CWE191_Integer_Underflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE191_Integer_Underflow__int_random_multiply_66a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = (new SecureRandom()).nextInt();
        int[] dataArray = new int[5];
        dataArray[2] = data;
        (new CWE191_Integer_Underflow__int_random_multiply_66b()).badSink(dataArray  );
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
        (new CWE191_Integer_Underflow__int_random_multiply_66b()).goodG2BSink(dataArray  );
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = (new SecureRandom()).nextInt();
        int[] dataArray = new int[5];
        dataArray[2] = data;
        (new CWE191_Integer_Underflow__int_random_multiply_66b()).goodB2GSink(dataArray  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
