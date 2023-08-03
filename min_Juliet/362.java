
package testcases.CWE789_Uncontrolled_Mem_Alloc.s01;
import testcasesupport.*;
import java.util.LinkedList;
import javax.servlet.http.*;
import java.util.HashMap;
public class CWE789_Uncontrolled_Mem_Alloc__Environment_HashMap_73b
{
    public void badSink(LinkedList<Integer> dataLinkedList ) throws Throwable
    {
        int data = dataLinkedList.remove(2);
        HashMap intHashMap = new HashMap(data);
    }
    public void goodG2BSink(LinkedList<Integer> dataLinkedList ) throws Throwable
    {
        int data = dataLinkedList.remove(2);
        HashMap intHashMap = new HashMap(data);
    }
}
