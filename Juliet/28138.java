
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
public class CWE369_Divide_by_Zero__float_zero_modulo_45 extends AbstractTestCase
{
    private float dataBad;
    private float dataGoodG2B;
    private float dataGoodB2G;
    private void badSink() throws Throwable
    {
        float data = dataBad;
        int result = (int)(100.0 % data);
        IO.writeLine(result);
    }
    public void bad() throws Throwable
    {
        float data;
        data = 0.0f; 
        dataBad = data;
        badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2BSink() throws Throwable
    {
        float data = dataGoodG2B;
        int result = (int)(100.0 % data);
        IO.writeLine(result);
    }
    private void goodG2B() throws Throwable
    {
        float data;
        data = 2.0f;
        dataGoodG2B = data;
        goodG2BSink();
    }
    private void goodB2GSink() throws Throwable
    {
        float data = dataGoodB2G;
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
        dataGoodB2G = data;
        goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
