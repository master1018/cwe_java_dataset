
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE369_Divide_by_Zero__float_random_modulo_21 extends AbstractTestCase
{
    private boolean badPrivate = false;
    public void bad() throws Throwable
    {
        float data;
        SecureRandom secureRandom = new SecureRandom();
        data = secureRandom.nextFloat();
        badPrivate = true;
        badSink(data );
    }
    private void badSink(float data ) throws Throwable
    {
        if (badPrivate)
        {
            int result = (int)(100.0 % data);
            IO.writeLine(result);
        }
    }
    private boolean goodB2G1Private = false;
    private boolean goodB2G2Private = false;
    private boolean goodG2BPrivate = false;
    public void good() throws Throwable
    {
        goodB2G1();
        goodB2G2();
        goodG2B();
    }
    private void goodB2G1() throws Throwable
    {
        float data;
        SecureRandom secureRandom = new SecureRandom();
        data = secureRandom.nextFloat();
        goodB2G1Private = false;
        goodB2G1Sink(data );
    }
    private void goodB2G1Sink(float data ) throws Throwable
    {
        if (goodB2G1Private)
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
        SecureRandom secureRandom = new SecureRandom();
        data = secureRandom.nextFloat();
        goodB2G2Private = true;
        goodB2G2Sink(data );
    }
    private void goodB2G2Sink(float data ) throws Throwable
    {
        if (goodB2G2Private)
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
    private void goodG2B() throws Throwable
    {
        float data;
        data = 2.0f;
        goodG2BPrivate = true;
        goodG2BSink(data );
    }
    private void goodG2BSink(float data ) throws Throwable
    {
        if (goodG2BPrivate)
        {
            int result = (int)(100.0 % data);
            IO.writeLine(result);
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
