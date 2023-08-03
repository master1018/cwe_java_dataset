
package testcases.CWE369_Divide_by_Zero.s01;
import testcasesupport.*;
import java.util.HashMap;
public class CWE369_Divide_by_Zero__float_File_divide_74b
{
    public void badSink(HashMap<Integer,Float> dataHashMap ) throws Throwable
    {
        float data = dataHashMap.get(2);
        int result = (int)(100.0 / data);
        IO.writeLine(result);
    }
    public void goodG2BSink(HashMap<Integer,Float> dataHashMap ) throws Throwable
    {
        float data = dataHashMap.get(2);
        int result = (int)(100.0 / data);
        IO.writeLine(result);
    }
    public void goodB2GSink(HashMap<Integer,Float> dataHashMap ) throws Throwable
    {
        float data = dataHashMap.get(2);
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
