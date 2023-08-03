
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
import java.util.LinkedList;
public class CWE476_NULL_Pointer_Dereference__StringBuilder_73b
{
    public void badSink(LinkedList<StringBuilder> dataLinkedList ) throws Throwable
    {
        StringBuilder data = dataLinkedList.remove(2);
        IO.writeLine("" + data.length());
    }
    public void goodG2BSink(LinkedList<StringBuilder> dataLinkedList ) throws Throwable
    {
        StringBuilder data = dataLinkedList.remove(2);
        IO.writeLine("" + data.length());
    }
    public void goodB2GSink(LinkedList<StringBuilder> dataLinkedList ) throws Throwable
    {
        StringBuilder data = dataLinkedList.remove(2);
        if (data != null)
        {
            IO.writeLine("" + data.length());
        }
        else
        {
            IO.writeLine("data is null");
        }
    }
}
