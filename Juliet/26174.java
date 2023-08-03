
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
public class CWE369_Divide_by_Zero__float_zero_modulo_08 extends AbstractTestCase
{
    private boolean privateReturnsTrue()
    {
        return true;
    }
    private boolean privateReturnsFalse()
    {
        return false;
    }
    public void bad() throws Throwable
    {
        float data;
        if (privateReturnsTrue())
        {
            data = 0.0f; 
        }
        else
        {
            data = 0.0f;
        }
        if (privateReturnsTrue())
        {
            int result = (int)(100.0 % data);
            IO.writeLine(result);
        }
    }
    private void goodG2B1() throws Throwable
    {
        float data;
        if (privateReturnsFalse())
        {
            data = 0.0f;
        }
        else
        {
            data = 2.0f;
        }
        if (privateReturnsTrue())
        {
            int result = (int)(100.0 % data);
            IO.writeLine(result);
        }
    }
    private void goodG2B2() throws Throwable
    {
        float data;
        if (privateReturnsTrue())
        {
            data = 2.0f;
        }
        else
        {
            data = 0.0f;
        }
        if (privateReturnsTrue())
        {
            int result = (int)(100.0 % data);
            IO.writeLine(result);
        }
    }
    private void goodB2G1() throws Throwable
    {
        float data;
        if (privateReturnsTrue())
        {
            data = 0.0f; 
        }
        else
        {
            data = 0.0f;
        }
        if (privateReturnsFalse())
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
        if (privateReturnsTrue())
        {
            data = 0.0f; 
        }
        else
        {
            data = 0.0f;
        }
        if (privateReturnsTrue())
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
