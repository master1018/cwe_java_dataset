
package testcases.CWE400_Resource_Exhaustion.s01;
import testcasesupport.*;
import java.util.LinkedList;
import javax.servlet.http.*;
public class CWE400_Resource_Exhaustion__getQueryString_Servlet_for_loop_73b
{
    public void badSink(LinkedList<Integer> countLinkedList , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count = countLinkedList.remove(2);
        int i = 0;
        for (i = 0; i < count; i++)
        {
            IO.writeLine("Hello");
        }
    }
    public void goodG2BSink(LinkedList<Integer> countLinkedList , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count = countLinkedList.remove(2);
        int i = 0;
        for (i = 0; i < count; i++)
        {
            IO.writeLine("Hello");
        }
    }
    public void goodB2GSink(LinkedList<Integer> countLinkedList , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count = countLinkedList.remove(2);
        int i = 0;
        if (count > 0 && count <= 20)
        {
            for (i = 0; i < count; i++)
            {
                IO.writeLine("Hello");
            }
        }
    }
}
