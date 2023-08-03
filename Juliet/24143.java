
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE369_Divide_by_Zero__float_random_divide_17 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        float data;
        SecureRandom secureRandom = new SecureRandom();
        data = secureRandom.nextFloat();
        for (int j = 0; j < 1; j++)
        {
            int result = (int)(100.0 / data);
            IO.writeLine(result);
        }
    }
    private void goodG2B() throws Throwable
    {
        float data;
        data = 2.0f;
        for (int j = 0; j < 1; j++)
        {
            int result = (int)(100.0 / data);
            IO.writeLine(result);
        }
    }
    private void goodB2G() throws Throwable
    {
        float data;
        SecureRandom secureRandom = new SecureRandom();
        data = secureRandom.nextFloat();
        for (int k = 0; k < 1; k++)
        {
            if (Math.abs(data) > 0.000001)
            {
                int result = (int)(100.0 / data);
                IO.writeLine(result);
            }
            else
            {
                IO.writeLine("This would result in a divide by zero");
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
