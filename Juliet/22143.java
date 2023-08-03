
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
import java.util.LinkedList;
public class CWE476_NULL_Pointer_Dereference__int_array_73b
{
    public void badSink(LinkedList<int []> dataLinkedList ) throws Throwable
    {
        int [] data = dataLinkedList.remove(2);
        IO.writeLine("" + data.length);
    }
    public void goodG2BSink(LinkedList<int []> dataLinkedList ) throws Throwable
    {
        int [] data = dataLinkedList.remove(2);
        IO.writeLine("" + data.length);
    }
    public void goodB2GSink(LinkedList<int []> dataLinkedList ) throws Throwable
    {
        int [] data = dataLinkedList.remove(2);
        if (data != null)
        {
            IO.writeLine("" + data.length);
        }
        else
        {
            IO.writeLine("data is null");
        }
    }
}
