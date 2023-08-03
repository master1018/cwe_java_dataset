
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE369_Divide_by_Zero__float_random_modulo_31 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        float dataCopy;
        {
            float data;
            SecureRandom secureRandom = new SecureRandom();
            data = secureRandom.nextFloat();
            dataCopy = data;
        }
        {
            float data = dataCopy;
            int result = (int)(100.0 % data);
            IO.writeLine(result);
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        float dataCopy;
        {
            float data;
            data = 2.0f;
            dataCopy = data;
        }
        {
            float data = dataCopy;
            int result = (int)(100.0 % data);
            IO.writeLine(result);
        }
    }
    private void goodB2G() throws Throwable
    {
        float dataCopy;
        {
            float data;
            SecureRandom secureRandom = new SecureRandom();
            data = secureRandom.nextFloat();
            dataCopy = data;
        }
        {
            float data = dataCopy;
            if (Math.abs(data) > 0.000001)
            {
                int result = (int)(100.0 % data);
                IO.writeLine(result);
            }
            else
            {
                IO.writeLine("This would result in a modulo by zero");
            }
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
