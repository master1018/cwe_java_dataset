
package testcases.CWE400_Resource_Exhaustion.s03;
import testcasesupport.*;
import java.util.LinkedList;
public class CWE400_Resource_Exhaustion__sleep_Property_73b
{
    public void badSink(LinkedList<Integer> countLinkedList ) throws Throwable
    {
        int count = countLinkedList.remove(2);
        Thread.sleep(count);
    }
    public void goodG2BSink(LinkedList<Integer> countLinkedList ) throws Throwable
    {
        int count = countLinkedList.remove(2);
        Thread.sleep(count);
    }
    public void goodB2GSink(LinkedList<Integer> countLinkedList ) throws Throwable
    {
        int count = countLinkedList.remove(2);
        if (count > 0 && count <= 2000)
        {
            Thread.sleep(count);
        }
    }
}
