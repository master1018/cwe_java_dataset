
package testcases.CWE129_Improper_Validation_of_Array_Index.s05;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE129_Improper_Validation_of_Array_Index__random_array_write_no_check_41 extends AbstractTestCase
{
    private void badSink(int data ) throws Throwable
    {
        int array[] = { 0, 1, 2, 3, 4 };
        array[data] = 42;
    }
    public void bad() throws Throwable
    {
        int data;
        data = (new SecureRandom()).nextInt();
        badSink(data  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2BSink(int data ) throws Throwable
    {
        int array[] = { 0, 1, 2, 3, 4 };
        array[data] = 42;
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        goodG2BSink(data  );
    }
    private void goodB2GSink(int data ) throws Throwable
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
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = (new SecureRandom()).nextInt();
        goodB2GSink(data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
