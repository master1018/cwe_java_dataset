
package testcases.CWE369_Divide_by_Zero.s01;
import testcasesupport.*;
import java.util.LinkedList;
public class CWE369_Divide_by_Zero__float_File_divide_73b
{
    public void badSink(LinkedList<Float> dataLinkedList ) throws Throwable
    {
        float data = dataLinkedList.remove(2);
        int result = (int)(100.0 / data);
        IO.writeLine(result);
    }
    public void goodG2BSink(LinkedList<Float> dataLinkedList ) throws Throwable
    {
        float data = dataLinkedList.remove(2);
        int result = (int)(100.0 / data);
        IO.writeLine(result);
    }
    public void goodB2GSink(LinkedList<Float> dataLinkedList ) throws Throwable
    {
        float data = dataLinkedList.remove(2);
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
