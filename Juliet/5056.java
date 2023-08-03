
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
import java.util.LinkedList;
public class CWE476_NULL_Pointer_Dereference__Integer_73b
{
    public void badSink(LinkedList<Integer> dataLinkedList ) throws Throwable
    {
        Integer data = dataLinkedList.remove(2);
        IO.writeLine("" + data.toString());
    }
    public void goodG2BSink(LinkedList<Integer> dataLinkedList ) throws Throwable
    {
        Integer data = dataLinkedList.remove(2);
        IO.writeLine("" + data.toString());
    }
    public void goodB2GSink(LinkedList<Integer> dataLinkedList ) throws Throwable
    {
        Integer data = dataLinkedList.remove(2);
        if (data != null)
        {
            IO.writeLine("" + data.toString());
        }
        else
        {
            IO.writeLine("data is null");
        }
    }
}
