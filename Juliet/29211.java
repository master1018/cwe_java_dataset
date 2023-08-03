
package testcases.CWE369_Divide_by_Zero.s01;
import testcasesupport.*;
public class CWE369_Divide_by_Zero__float_File_modulo_54e
{
    public void badSink(float data ) throws Throwable
    {
        int result = (int)(100.0 % data);
        IO.writeLine(result);
    }
    public void goodG2BSink(float data ) throws Throwable
    {
        int result = (int)(100.0 % data);
        IO.writeLine(result);
    }
    public void goodB2GSink(float data ) throws Throwable
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
