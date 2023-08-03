
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
public class CWE369_Divide_by_Zero__float_zero_modulo_41 extends AbstractTestCase
{
    private void badSink(float data ) throws Throwable
    {
        int result = (int)(100.0 % data);
        IO.writeLine(result);
    }
    public void bad() throws Throwable
    {
        float data;
        data = 0.0f; 
        badSink(data  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2BSink(float data ) throws Throwable
    {
        int result = (int)(100.0 % data);
        IO.writeLine(result);
    }
    private void goodG2B() throws Throwable
    {
        float data;
        data = 2.0f;
        goodG2BSink(data  );
    }
    private void goodB2GSink(float data ) throws Throwable
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
    private void goodB2G() throws Throwable
    {
        float data;
        data = 0.0f; 
        goodB2GSink(data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
