
package testcases.CWE369_Divide_by_Zero.s03;
import testcasesupport.*;
import java.util.LinkedList;
import javax.servlet.http.*;
public class CWE369_Divide_by_Zero__int_Property_divide_73b
{
    public void badSink(LinkedList<Integer> dataLinkedList ) throws Throwable
    {
        int data = dataLinkedList.remove(2);
        IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
    }
    public void goodG2BSink(LinkedList<Integer> dataLinkedList ) throws Throwable
    {
        int data = dataLinkedList.remove(2);
        IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
    }
    public void goodB2GSink(LinkedList<Integer> dataLinkedList ) throws Throwable
    {
        int data = dataLinkedList.remove(2);
        if (data != 0)
        {
            IO.writeLine("100/" + data + " = " + (100 / data) + "\n");
        }
        else
        {
            IO.writeLine("This would result in a divide by zero");
        }
    }
}
