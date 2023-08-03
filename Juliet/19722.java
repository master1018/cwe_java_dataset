
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE191_Integer_Underflow__int_database_sub_61a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data = (new CWE191_Integer_Underflow__int_database_sub_61b()).badSource();
        int result = (int)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int data = (new CWE191_Integer_Underflow__int_database_sub_61b()).goodG2BSource();
        int result = (int)(data - 1);
        IO.writeLine("result: " + result);
    }
    private void goodB2G() throws Throwable
    {
        int data = (new CWE191_Integer_Underflow__int_database_sub_61b()).goodB2GSource();
        if (data > Integer.MIN_VALUE)
        {
            int result = (int)(data - 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too small to perform subtraction.");
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
