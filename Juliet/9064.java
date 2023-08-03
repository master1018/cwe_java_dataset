
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE369_Divide_by_Zero__float_random_divide_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        float data;
        if(IO.staticReturnsTrueOrFalse())
        {
            SecureRandom secureRandom = new SecureRandom();
            data = secureRandom.nextFloat();
        }
        else
        {
            data = 2.0f;
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            int result = (int)(100.0 / data);
            IO.writeLine(result);
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
    private void goodG2B() throws Throwable
    {
        float data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = 2.0f;
        }
        else
        {
            data = 2.0f;
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            int result = (int)(100.0 / data);
            IO.writeLine(result);
        }
        else
        {
            int result = (int)(100.0 / data);
            IO.writeLine(result);
        }
    }
    private void goodB2G() throws Throwable
    {
        float data;
        if(IO.staticReturnsTrueOrFalse())
        {
            SecureRandom secureRandom = new SecureRandom();
            data = secureRandom.nextFloat();
        }
        else
        {
            SecureRandom secureRandom = new SecureRandom();
            data = secureRandom.nextFloat();
        }
        if(IO.staticReturnsTrueOrFalse())
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
