
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
public class CWE369_Divide_by_Zero__float_URLConnection_modulo_66b
{
    public void badSink(float dataArray[] ) throws Throwable
    {
        float data = dataArray[2];
        int result = (int)(100.0 % data);
        IO.writeLine(result);
    }
    public void goodG2BSink(float dataArray[] ) throws Throwable
    {
        float data = dataArray[2];
        int result = (int)(100.0 % data);
        IO.writeLine(result);
    }
    public void goodB2GSink(float dataArray[] ) throws Throwable
    {
        float data = dataArray[2];
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
