
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE369_Divide_by_Zero__float_random_divide_11 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        float data;
        if (IO.staticReturnsTrue())
        {
            SecureRandom secureRandom = new SecureRandom();
            data = secureRandom.nextFloat();
        }
        else
        {
            data = 0.0f;
        }
        if(IO.staticReturnsTrue())
        {
            int result = (int)(100.0 / data);
            IO.writeLine(result);
        }
    }
    private void goodG2B1() throws Throwable
    {
        float data;
        if (IO.staticReturnsFalse())
        {
            data = 0.0f;
        }
        else
        {
            data = 2.0f;
        }
        if (IO.staticReturnsTrue())
        {
            int result = (int)(100.0 / data);
            IO.writeLine(result);
        }
    }
    private void goodG2B2() throws Throwable
    {
        float data;
        if (IO.staticReturnsTrue())
        {
            data = 2.0f;
        }
        else
        {
            data = 0.0f;
        }
        if (IO.staticReturnsTrue())
        {
            int result = (int)(100.0 / data);
            IO.writeLine(result);
        }
    }
    private void goodB2G1() throws Throwable
    {
        float data;
        if (IO.staticReturnsTrue())
        {
            SecureRandom secureRandom = new SecureRandom();
            data = secureRandom.nextFloat();
        }
        else
        {
            data = 0.0f;
        }
        if (IO.staticReturnsFalse())
        {
            IO.writeLine("Benign, fixed string");
        }
        else
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
    private void goodB2G2() throws Throwable
    {
        float data;
        if (IO.staticReturnsTrue())
        {
            SecureRandom secureRandom = new SecureRandom();
            data = secureRandom.nextFloat();
        }
        else
        {
            data = 0.0f;
        }
        if (IO.staticReturnsTrue())
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