
package testcases.CWE190_Integer_Overflow.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE190_Integer_Overflow__int_random_square_17 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = (new SecureRandom()).nextInt();
        for (int j = 0; j < 1; j++)
        {
            int result = (int)(data * data);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        for (int j = 0; j < 1; j++)
        {
            int result = (int)(data * data);
            IO.writeLine("result: " + result);
        }
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = (new SecureRandom()).nextInt();
        for (int k = 0; k < 1; k++)
        {
            if ((data != Integer.MIN_VALUE) && (data != Long.MIN_VALUE) && (Math.abs(data) <= (long)Math.sqrt(Integer.MAX_VALUE)))
            {
                int result = (int)(data * data);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform squaring.");
            }
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
