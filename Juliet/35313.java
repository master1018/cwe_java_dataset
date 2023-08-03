
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
public class CWE369_Divide_by_Zero__float_zero_modulo_42 extends AbstractTestCase
{
    private float badSource() throws Throwable
    {
        float data;
        data = 0.0f; 
        return data;
    }
    public void bad() throws Throwable
    {
        float data = badSource();
        int result = (int)(100.0 % data);
        IO.writeLine(result);
    }
    private float goodG2BSource() throws Throwable
    {
        float data;
        data = 2.0f;
        return data;
    }
    private void goodG2B() throws Throwable
    {
        float data = goodG2BSource();
        int result = (int)(100.0 % data);
        IO.writeLine(result);
    }
    private float goodB2GSource() throws Throwable
    {
        float data;
        data = 0.0f; 
        return data;
    }
    private void goodB2G() throws Throwable
    {
        float data = goodB2GSource();
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
