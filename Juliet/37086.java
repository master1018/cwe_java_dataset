
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE369_Divide_by_Zero__float_random_modulo_10 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        float data;
        if (IO.staticTrue)
        {
            SecureRandom secureRandom = new SecureRandom();
            data = secureRandom.nextFloat();
        }
        else
        {
            data = 0.0f;
        }
        if (IO.staticTrue)
        {
            int result = (int)(100.0 % data);
            IO.writeLine(result);
        }
    }
    private void goodG2B1() throws Throwable
    {
        float data;
        if (IO.staticFalse)
        {
            data = 0.0f;
        }
        else
        {
            data = 2.0f;
        }
        if (IO.staticTrue)
        {
            int result = (int)(100.0 % data);
            IO.writeLine(result);
        }
    }
    private void goodG2B2() throws Throwable
    {
        float data;
        if (IO.staticTrue)
        {
            data = 2.0f;
        }
        else
        {
            data = 0.0f;
        }
        if (IO.staticTrue)
        {
            int result = (int)(100.0 % data);
            IO.writeLine(result);
        }
    }
    private void goodB2G1() throws Throwable
    {
        float data;
        if (IO.staticTrue)
        {
            SecureRandom secureRandom = new SecureRandom();
            data = secureRandom.nextFloat();
        }
        else
        {
            data = 0.0f;
        }
        if (IO.staticFalse)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
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
    private void goodB2G2() throws Throwable
    {
        float data;
        if (IO.staticTrue)
        {
            SecureRandom secureRandom = new SecureRandom();
            data = secureRandom.nextFloat();
        }
        else
        {
            data = 0.0f;
        }
        if (IO.staticTrue)
        {
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
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
        goodB2G1();
        goodB2G2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
