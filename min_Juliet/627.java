
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
import java.util.Vector;
public class CWE369_Divide_by_Zero__float_URLConnection_divide_72b
{
    public void badSink(Vector<Float> dataVector ) throws Throwable
    {
        float data = dataVector.remove(2);
        int result = (int)(100.0 / data);
        IO.writeLine(result);
    }
    public void goodG2BSink(Vector<Float> dataVector ) throws Throwable
    {
        float data = dataVector.remove(2);
        int result = (int)(100.0 / data);
        IO.writeLine(result);
    }
    public void goodB2GSink(Vector<Float> dataVector ) throws Throwable
    {
        float data = dataVector.remove(2);
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
