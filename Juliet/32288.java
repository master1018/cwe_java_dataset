
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import java.util.LinkedList;
public class CWE690_NULL_Deref_From_Return__Class_StringBuilder_73b
{
    public void badSink(LinkedList<StringBuilder> dataLinkedList ) throws Throwable
    {
        StringBuilder data = dataLinkedList.remove(2);
        String stringTrimmed = data.toString().trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodG2BSink(LinkedList<StringBuilder> dataLinkedList ) throws Throwable
    {
        StringBuilder data = dataLinkedList.remove(2);
        String stringTrimmed = data.toString().trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodB2GSink(LinkedList<StringBuilder> dataLinkedList ) throws Throwable
    {
        StringBuilder data = dataLinkedList.remove(2);
        if (data != null)
        {
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
        }
    }
}
