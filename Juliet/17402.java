
package testcases.CWE190_Integer_Overflow.s03;
import testcasesupport.*;
import java.util.LinkedList;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_Property_add_73b
{
    public void badSink(LinkedList<Integer> dataLinkedList ) throws Throwable
    {
        int data = dataLinkedList.remove(2);
        int result = (int)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(LinkedList<Integer> dataLinkedList ) throws Throwable
    {
        int data = dataLinkedList.remove(2);
        int result = (int)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(LinkedList<Integer> dataLinkedList ) throws Throwable
    {
        int data = dataLinkedList.remove(2);
        if (data < Integer.MAX_VALUE)
        {
            int result = (int)(data + 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too large to perform addition.");
        }
    }
}
