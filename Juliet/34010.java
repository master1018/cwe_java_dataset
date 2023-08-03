
package testcases.CWE369_Divide_by_Zero.s01;
import testcasesupport.*;
public class CWE369_Divide_by_Zero__float_listen_tcp_divide_71b
{
    public void badSink(Object dataObject ) throws Throwable
    {
        float data = (Float)dataObject;
        int result = (int)(100.0 / data);
        IO.writeLine(result);
    }
    public void goodG2BSink(Object dataObject ) throws Throwable
    {
        float data = (Float)dataObject;
        int result = (int)(100.0 / data);
        IO.writeLine(result);
    }
    public void goodB2GSink(Object dataObject ) throws Throwable
    {
        float data = (Float)dataObject;
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
